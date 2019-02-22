package com.ltdd.domin.interactor.login;

import com.ltdd.domin.constant.Constant;
import com.ltdd.domin.modules.BasicResponse;
import com.ltdd.domin.exception.ApiException;
import com.ltdd.domin.interactor.UseCase;
import com.ltdd.domin.repository.LoginRepository;

import org.reactivestreams.Publisher;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

/**
 * @author lpy
 * @date 2019/2/22 11:22
 * @description 登录
 */
public class LoginUserCase extends UseCase<BasicResponse, LoginUserCase.Params> {
    private final LoginRepository mLoginRepository;
    
    @Inject
    public LoginUserCase(LoginRepository loginRepository) {
        mLoginRepository = loginRepository;
    }
    
    @Override
    public Flowable<BasicResponse> buildUseCaseObservable(Params params) {
        return mLoginRepository.login(params.userName, params.userPwd)
                .flatMap(new Function<BasicResponse, Publisher<BasicResponse>>() {
                    @Override
                    public Publisher<BasicResponse> apply(BasicResponse basicResponse) throws Exception {
                        return handleData(basicResponse);
                    }
                });
    }
    
    private Publisher<BasicResponse> handleData(BasicResponse basicResponse) {
        if (basicResponse == null) {
            return Flowable.error(new ApiException(Constant.CODE_EMPTY, "server response body is null"));
        }
        if (basicResponse.code == Constant.CODE_SUCCESS) {
            return Flowable.just(basicResponse);
        } else {
            return Flowable.error(new ApiException(Constant.CODE_BIZ_ERROR, basicResponse.msg));
        }
    }
    
    public static final class Params {
        private String userName;
        private String userPwd;
        
        public String getUserName() {
            return userName;
        }
        
        public void setUserName(String userName) {
            this.userName = userName;
        }
        
        public String getUserPwd() {
            return userPwd;
        }
        
        public void setUserPwd(String userPwd) {
            this.userPwd = userPwd;
        }
    }
}
