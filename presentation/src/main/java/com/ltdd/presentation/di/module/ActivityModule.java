package com.ltdd.presentation.di.module;

import android.app.Activity;


import com.ltdd.presentation.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * @author lpy
 * @date 2019/2/22 12:07
 * @description
 */
@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    @ActivityScope
    Activity provideActivity() {
        return mActivity;
    }
}
