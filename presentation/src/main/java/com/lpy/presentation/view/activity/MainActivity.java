package com.lpy.presentation.view.activity;


import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.blankj.utilcode.util.LogUtils;
import com.example.presentation.R;
import com.lpy.domin.entity.UserInfo;
import com.lpy.presentation.base.BaseActivity;
import com.lpy.presentation.hook.EvilInstrumentation;
import com.lpy.presentation.presenter.MainPresenter;
import com.lpy.presentation.presenter.contract.MainContract;
import com.lpy.presentation.view.service.MyMqttService;


import java.lang.reflect.Field;
import java.lang.reflect.Method;

import butterknife.OnClick;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {
    
    
    @Override
    protected void initInject() {
        super.initInject();
        getActivityComponent().inject(this);
    }
    
    @Override
    protected void initView() {
        super.initView();
        init();
    }
    
    private void init() {
        try {
            // 先获取到当前的ActivityThread对象
            Class<?> aClass = Class.forName("android.app.ActivityThread");
            Method currentActivityThread = aClass.getDeclaredMethod("currentActivityThread");
            currentActivityThread.setAccessible(true);
            Object invoke = currentActivityThread.invoke(null);
            // 拿到原始的 mInstrumentation字段
            Field mInstrumentation = aClass.getDeclaredField("mInstrumentation");
            mInstrumentation.setAccessible(true);
            Instrumentation i = (Instrumentation) mInstrumentation.get(invoke);
            
            EvilInstrumentation evilInstrumentation = new EvilInstrumentation(i);
            mInstrumentation.set(invoke, evilInstrumentation);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
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
        getApplicationContext().startActivity(new Intent(this, PahoExampleActivity.class).setFlags(FLAG_ACTIVITY_NEW_TASK));
        mPresenter.login();
    }
}
