package com.lpy.domin.repository;

import com.lpy.domin.modules.BasicResponse;

import io.reactivex.Flowable;

public interface LoginRepository {
    Flowable<BasicResponse> login(String memberAccount, String memberPwd);
}
