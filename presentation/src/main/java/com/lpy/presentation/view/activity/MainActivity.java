package com.lpy.presentation.view.activity;

import android.os.Bundle;
import android.widget.Button;

import com.blankj.utilcode.util.LogUtils;
import com.example.presentation.R;
import com.lpy.domin.entity.UserInfo;
import com.lpy.presentation.base.BaseActivity;
import com.lpy.presentation.presenter.MainPresenter;
import com.lpy.presentation.presenter.contract.MainContract;

import butterknife.OnClick;


public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {
    
    
    @Override
    protected void initInject() {
        super.initInject();
        getActivityComponent().inject(this);
    }
    
    @Override
    protected int getContentResId() {
        return R.layout.activity_main;
    }
    
    @Override
    public void showUser(UserInfo userInfo) {
        LogUtils.e(userInfo.toString());
        
    }
    
    @OnClick(R.id.login)
    public void login() {
        mPresenter.login();
    }
}
