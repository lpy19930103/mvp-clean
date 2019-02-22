package com.ltdd.presentation.di.component;

import android.app.Activity;

import com.ltdd.presentation.di.module.FragmentModule;
import com.ltdd.presentation.di.scope.FragmentScope;

import dagger.Component;

/**
 * @author lpy
 * @date 2019/2/22 14:10
 * @description
 */
@FragmentScope
@Component(dependencies = AppComponent.class, modules = {FragmentModule.class})
public interface FragmentComponent {

    Activity getActivity();

//    void inject(HomeFragment homeFragment);

}
