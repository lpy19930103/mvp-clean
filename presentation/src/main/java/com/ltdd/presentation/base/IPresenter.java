package com.ltdd.presentation.base;

/**
 * @author lpy
 * @date 2019/2/22 11:44
 * @description
 */
public interface IPresenter<T extends IView> {
    void attachView(T view);

    void detachView();
}
