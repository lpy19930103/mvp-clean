package com.ltdd.presentation.di.component;

import android.app.Activity;


import com.ltdd.presentation.view.activity.MainActivity;
import com.ltdd.presentation.di.module.ActivityModule;
import com.ltdd.presentation.di.scope.ActivityScope;

import dagger.Component;

 /**
  * @author lpy
  * @date 2019/2/22 13:48
  * @description 
  */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class})
public interface ActivityComponent {
    
    Activity getActivity();

    void inject(MainActivity mainActivity);
}
