package com.lpy.domin.repository;

import com.lpy.domin.entity.BasicResponse;
import com.lpy.domin.entity.UserInfo;

import io.reactivex.Flowable;

public interface LoginRepository {
    Flowable<BasicResponse<UserInfo>> login(String memberAccount, String memberPwd);
}
