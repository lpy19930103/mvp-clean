package com.lpy.presentation.base;

/**
 * @author lpy
 * @date 2019/2/22 11:44
 * @description
 */
public interface IView {
    void showError(String msg);
    void showLoading();
    void hideLoading();
}
