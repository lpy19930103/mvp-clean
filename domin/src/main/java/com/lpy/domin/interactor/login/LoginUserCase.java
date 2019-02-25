package com.lpy.domin.interactor.login;

import com.lpy.domin.entity.BasicResponse;
import com.lpy.domin.entity.UserInfo;
import com.lpy.domin.interactor.UseCase;
import com.lpy.domin.repository.LoginRepository;

import org.reactivestreams.Publisher;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

/**
 * @author lpy
 * @date 2019/2/22 11:22
 * @description 登录
 */
public class LoginUserCase extends UseCase<UserInfo, LoginUserCase.Params> {
    private final LoginRepository mLoginRepository;
    
    @Inject
    public LoginUserCase(LoginRepository loginRepository) {
        mLoginRepository = loginRepository;
    }
    
    @Override
    public Flowable<UserInfo> buildUseCaseObservable(Params params) {
        return mLoginRepository.login(params.userName, params.userPwd)
                .flatMap(new Function<BasicResponse<UserInfo>, Publisher<UserInfo>>() {
                    @Override
                    public Publisher<UserInfo> apply(BasicResponse<UserInfo> userInfoBasicResponse) throws Exception {
                        return handleData(userInfoBasicResponse);
                    }
                });
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
