package com.ltdd.domin.repository;

import com.ltdd.domin.modules.BasicResponse;

import io.reactivex.Flowable;

public interface LoginRepository {
    Flowable<BasicResponse> login(String memberAccount, String memberPwd);
}
