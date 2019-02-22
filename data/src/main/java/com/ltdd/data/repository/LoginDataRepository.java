package com.ltdd.data.repository;

import com.ltdd.domin.modules.BasicResponse;
import com.ltdd.data.api.HttpHelper;
import com.ltdd.data.service.RestService;
import com.ltdd.domin.repository.LoginRepository;

import javax.inject.Inject;

import io.reactivex.Flowable;

public class LoginDataRepository implements LoginRepository {
    
    private RestService restService;
    
    @Inject
    public LoginDataRepository(HttpHelper httpHelper) {
        restService = httpHelper.getRestApi();
    }
    
    
    @Override
    public Flowable<BasicResponse> login(String memberAccount, String memberPwd) {
        return restService.login(memberAccount, memberPwd, "12", "1");
    }
}
