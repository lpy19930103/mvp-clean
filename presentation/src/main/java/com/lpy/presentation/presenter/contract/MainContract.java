package com.lpy.presentation.presenter.contract;

import com.lpy.domin.entity.UserInfo;
import com.lpy.presentation.base.IPresenter;
import com.lpy.presentation.base.IView;

public interface MainContract {
    interface View extends IView {
        void showUser(UserInfo userInfo);
    }
    
    interface Presenter extends IPresenter<View> {
        UserInfo login();
    }
    
}
