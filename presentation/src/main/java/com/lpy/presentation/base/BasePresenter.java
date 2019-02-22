package com.lpy.presentation.base;

/**
 * @author lpy
 * @date 2019/2/22 16:21
 * @description
 */
public class BasePresenter<T extends IView> implements IPresenter<T> {
    protected T mView;
    
    @Override
    public void attachView(T view) {
        mView = view;
    }
    
    @Override
    public void detachView() {
    
    }
}
