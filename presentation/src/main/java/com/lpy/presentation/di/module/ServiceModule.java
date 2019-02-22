package com.lpy.presentation.di.module;

import android.app.Service;


import com.lpy.presentation.di.scope.ServiceScope;

import dagger.Module;
import dagger.Provides;

/**
 * @author lpy
 * @date 2019/2/22 13:48
 * @description
 */
@Module
public class ServiceModule {
    private Service mService;

    public ServiceModule(Service service) {
        this.mService = service;
    }

    @Provides
    @ServiceScope
    Service provideService() {
        return mService;
    }
}
