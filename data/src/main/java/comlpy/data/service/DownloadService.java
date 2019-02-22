package comlpy.data.service;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface DownloadService {
    
    @Streaming
    @GET
    Flowable<Response<ResponseBody>> download(@Url String url);
}
