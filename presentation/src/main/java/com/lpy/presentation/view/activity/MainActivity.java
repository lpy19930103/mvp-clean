package com.lpy.presentation.view.activity;


import android.content.ClipboardManager;
import android.content.Context;
import android.os.IBinder;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.example.presentation.R;
import com.lpy.domin.entity.UserInfo;
import com.lpy.presentation.base.BaseActivity;
import com.lpy.presentation.hook.BinderProxyHookHandler;
import com.lpy.presentation.presenter.MainPresenter;
import com.lpy.presentation.presenter.contract.MainContract;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

import butterknife.OnClick;


public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {
    
    
    @Override
    protected void initInject() {
        super.initInject();
        getActivityComponent().inject(this);
    }
    
    @Override
    protected void initView() {
        super.initView();
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
