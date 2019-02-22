package com.lpy.presentation.injector.component;

import android.app.Activity;

import com.lpy.presentation.injector.module.FragmentModule;
import com.lpy.presentation.injector.scope.FragmentScope;

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
