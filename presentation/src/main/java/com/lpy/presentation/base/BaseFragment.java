package com.lpy.presentation.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.lpy.presentation.app.App;
import com.lpy.presentation.di.component.DaggerFragmentComponent;
import com.lpy.presentation.di.component.FragmentComponent;
import com.lpy.presentation.di.module.FragmentModule;
import com.lpy.presentation.util.SnackbarUtil;

import javax.inject.Inject;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author lpy
 * @date 2019/2/22 11:44
 * @description Fragment基类
 */
public abstract class BaseFragment<T extends IPresenter> extends Fragment implements IView {

    @Inject
    protected T mPresenter;
    private Unbinder mUnBinder;
    protected AppCompatActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = (AppCompatActivity) getActivity();
        onPrepare();
        View rootView = inflater.inflate(getContentResId(), container, false);
        mUnBinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initListener();
        initData();
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroyView();
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
    }

    @Override
    public void showError(String msg) {
        SnackbarUtil.show(mActivity, msg);
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

    protected int getColor(@ColorRes int color) {
        return ContextCompat.getColor(mActivity, color);
    }


    protected void finish() {
        mActivity.finish();
    }



    /**
     * 返回布局资源id
     *
     * @return int
     */
    protected abstract int getContentResId();

    /**
     * 注入
     */
    protected void initInject() {
    }



    protected FragmentComponent getFragmentComponent() {
        return DaggerFragmentComponent.builder()
                .appComponent(App.getInstance().getAppComponent())
                .fragmentModule(getFragmentModule())
                .build();
    }

    private FragmentModule getFragmentModule() {
        return new FragmentModule(this);
    }
}
