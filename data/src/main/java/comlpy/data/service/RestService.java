package comlpy.data.service;

import com.lpy.domin.entity.BasicResponse;
import com.lpy.domin.entity.UserInfo;

import io.reactivex.Flowable;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RestService {
    
    
    String HOST = "http://192.168.0.5:81/";
    
    @Multipart
    @POST("user/login")
    Flowable<BasicResponse<UserInfo>> login(@Part("userName") String memberAccount, @Part("userPass") String memberPwd, @Part("logType") String logType, @Part("verificationType") String verificationType);
}
