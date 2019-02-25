package comlpy.data.convert;


import com.lpy.domin.constant.Constant;
import com.lpy.domin.entity.BasicResponse;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import io.reactivex.annotations.NonNull;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @author lpy
 * @date 2019/2/25 11:16
 * @description
 */
public class NobodyConverterFactory extends Converter.Factory {
    
    public static NobodyConverterFactory create() {
        return new NobodyConverterFactory();
    }
    
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        //判断当前的类型是否是我们需要处理的类型
        //是则创建一个Converter返回转换数据
        final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, type, annotations);
        return new Converter<ResponseBody, Object>() {
            @Override
            public Object convert(@NonNull ResponseBody value) throws IOException {
                if (value.contentLength() == 0)
                    return new BasicResponse(Constant.CODE_EMPTY, "server response body is null");
                return delegate.convert(value);
            }
        };
    }
    
    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return null;
    }
    
    @Override
    public Converter<?, String> stringConverter(Type type, Annotation[] annotations,
                                                Retrofit retrofit) {
        return null;
    }
}
