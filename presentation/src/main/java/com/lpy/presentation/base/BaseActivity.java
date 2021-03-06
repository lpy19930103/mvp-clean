package com.lpy.presentation.base;

import android.os.Bundle;

import com.lpy.presentation.app.App;
import com.lpy.presentation.injector.component.ActivityComponent;
import com.lpy.presentation.injector.component.DaggerActivityComponent;
import com.lpy.presentation.injector.module.ActivityModule;
import com.lpy.presentation.util.SnackbarUtil;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author lpy
 * @date 2019/2/22 11:40
 * @description 基类
 */
public abstract class BaseActivity<T extends IPresenter> extends AppCompatActivity implements IView {

    @Inject
    protected T mPresenter;
    private Unbinder mUnBinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentResId());
        mUnBinder = ButterKnife.bind(this);
        onPrepare();
        initView();
        initListener();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        mUnBinder.unbind();
    }

    @Override
    public void showError(String msg) {
        SnackbarUtil.show(this, msg);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @SuppressWarnings("unchecked")
    private void onPrepare() {
        initInject();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    protected void initListener() {

    }

    protected void initData() {

    }

    protected void initView() {

    }
    


    protected ActivityComponent getActivityComponent() {
        return DaggerActivityComponent.builder()
                .appComponent(App.getInstance().getAppComponent())
                .activityModule(getActivityModule())
                .build();
    }
    

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    /**
     * @return 返回布局资源id
     */
    protected abstract int getContentResId();

    /**
     * 注入
     */
    protected void initInject() {
    }
}