package com.lpy.presentation.injector.component;

import android.app.Activity;


import com.lpy.presentation.view.activity.MainActivity;
import com.lpy.presentation.injector.module.ActivityModule;
import com.lpy.presentation.injector.scope.ActivityScope;

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
