package com.lpy.presentation.presenter;

import com.lpy.domin.entity.UserInfo;
import com.lpy.domin.interactor.login.LoginUserCase;
import com.lpy.presentation.base.BasePresenter;
import com.lpy.presentation.injector.scope.ActivityScope;
import com.lpy.presentation.presenter.contract.MainContract;

import javax.inject.Inject;

import io.reactivex.subscribers.DisposableSubscriber;

@ActivityScope
public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {
    
    @Inject
    LoginUserCase mLoginUserCase;
    
    @Inject
    MainPresenter(LoginUserCase loginUserCase) {
        mLoginUserCase = loginUserCase;
    }
    
    @Override
    public UserInfo login() {
        LoginUserCase.Params params = new LoginUserCase.Params();
        params.setUserName("18519121233");
        params.setUserName("1851912121133");
        mLoginUserCase.execute(params, new DisposableSubscriber<UserInfo>() {
            @Override
            public void onNext(UserInfo userInfo) {
                mView.showUser(userInfo);
            }
            
            @Override
            public void onError(Throwable t) {
                mView.showError(t.getMessage());
            }
            
            @Override
            public void onComplete() {
            
            }
        });
        return null;
    }
}
