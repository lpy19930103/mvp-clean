
package comlpy.data.api;


import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;

import comlpy.data.bus.RxBus;
import comlpy.data.bus.event.DownloadEvent;
import comlpy.data.convert.NobodyConverterFactory;
import comlpy.data.service.DownloadService;
import comlpy.data.interceptor.HttpLoggingInterceptor;
import comlpy.data.service.RestService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Singleton
public class HttpHelper {
    
    private volatile RestService mRestService;
    private OkHttpClient mOkHttpClient;
    
    @Inject
    public HttpHelper(Context context) {
        if (mOkHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            // 设置缓存 20M
            /*Cache cache = new Cache(new File(context.getExternalCacheDir(), CacheConstant.CACHE_DIR_API), 20 * 1024 * 1024);
            builder.cache(cache);*/
            builder.addInterceptor(initLog());
            builder.addInterceptor(new CrazyDailyCacheInterceptor());
            builder.addNetworkInterceptor(new CrazyDailyCacheNetworkInterceptor());
            // 设置Cookie
//            builder.cookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context)));
            // 设置超时
            builder.connectTimeout(10, TimeUnit.SECONDS);
            builder.readTimeout(20, TimeUnit.SECONDS);
            builder.writeTimeout(20, TimeUnit.SECONDS);
            // 错误重连
            builder.retryOnConnectionFailure(true);
            mOkHttpClient = builder.build();
        }
    }
    
    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }
    
    private HttpLoggingInterceptor initLog() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("TL");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        loggingInterceptor.setColorLevel(Level.INFO);
        return loggingInterceptor;
    }
    
    public RestService getRestApi() {
        if (mRestService == null) {
            synchronized (this) {
                if (mRestService == null) {
                    mRestService = new Retrofit.Builder()
                            .baseUrl(RestService.HOST)
                            .client(mOkHttpClient)
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .addConverterFactory(NobodyConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build().create(RestService.class);
                }
            }
        }
        return mRestService;
    }
    
    
    public DownloadService getDownloadService(String host, int taskId) {
        return new Retrofit.Builder()
                .baseUrl(host)
                .client(new OkHttpClient.Builder()
                        .addInterceptor(initLog())
                        .addNetworkInterceptor(new ProgressInterceptor(taskId))
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(20, TimeUnit.SECONDS)
                        .writeTimeout(20, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)
                        .build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(DownloadService.class);
    }
    
    /**
     * 有网才会执行哦
     */
    private static class CrazyDailyCacheNetworkInterceptor implements Interceptor {
        
        private static final String CACHE_CONTROL = "Cache-Control";
        
        @Override
        public Response intercept(Chain chain) throws IOException {
            final Request request = chain.request();
            final Response response = chain.proceed(request);
            final String requestHeader = request.header(CACHE_CONTROL);
            //判断条件最好加上TextUtils.isEmpty(response.header(CACHE_CONTROL))来判断服务器是否返回缓存策略，如果返回，就按服务器的来，我这里全部客户端控制了
            if (!StringUtils.isEmpty(requestHeader)) {
                LogUtils.i("MSG_HTTP_CacheNetworkInterceptor---cache---host:" + request.url().host());
                return response.newBuilder().header(CACHE_CONTROL, requestHeader).removeHeader("Pragma").build();
            }
            return response;
        }
    }
    
    private static class CrazyDailyCacheInterceptor implements Interceptor {
        
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            CacheControl cacheControl = request.cacheControl();
            //header可控制不走这个逻辑
            boolean noCache = cacheControl.noCache() || cacheControl.noStore() || cacheControl.maxAgeSeconds() == 0;
     /*       if (!noCache && !NetworkUtils.isAvailableByPing()) {
                Request.Builder builder = request.newBuilder();
                LogUtils.i("MSG_HTTP_CacheNetworkInterceptor---cache---host:" + request.url().host());
                CacheControl newCacheControl = new CacheControl.Builder().maxStale(1, TimeUnit.DAYS).build();
                request = builder.cacheControl(newCacheControl).build();
                return chain.proceed(request);
            }*/
            return chain.proceed(request);
        }
    }
    
    private static class ProgressInterceptor implements Interceptor {
        
        private final int taskId;
        
        private ProgressInterceptor(int taskId) {
            this.taskId = taskId;
        }
        
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(taskId, originalResponse.body()))
                    .build();
        }
    }
    
    private static class ProgressResponseBody extends ResponseBody {
        private ResponseBody responseBody;
        private final int taskId;
        private BufferedSource bufferedSource;
        
        private ProgressResponseBody(int taskId, ResponseBody responseBody) {
            this.taskId = taskId;
            this.responseBody = responseBody;
        }
        
        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }
        
        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }
        
        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(contentLength(), responseBody.source()));
            }
            return bufferedSource;
        }
        
        private Source source(final long contentLength, Source source) {
            return new ForwardingSource(source) {
                long bytesReaded = 0;
                
                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    bytesReaded += bytesRead == -1 ? 0 : bytesRead;
                    RxBus.getDefault().post(String.valueOf(taskId), new DownloadEvent(taskId, contentLength, bytesReaded));
                    return bytesRead;
                }
            };
        }
    }
}
