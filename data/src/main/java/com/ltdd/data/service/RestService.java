package com.ltdd.data.service;

import com.ltdd.domin.modules.BasicResponse;

import io.reactivex.Flowable;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RestService {
    
    
    String HOST = "http://localhost:81/";
    
    @Multipart
    @POST("user/login")
    Flowable<BasicResponse> login(@Part("userName") String memberAccount, @Part("userPass") String memberPwd, @Part("logType") String logType, @Part("verificationType") String verificationType);
}
