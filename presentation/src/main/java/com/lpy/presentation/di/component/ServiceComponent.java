package com.lpy.presentation.di.component;

import android.app.Service;


import comlpy.data.service.DownloadService;
import com.lpy.presentation.di.module.ServiceModule;
import com.lpy.presentation.di.scope.ServiceScope;

import dagger.Component;

/**
 * @author lpy
 * @date 2019/2/22 13:50
 * @description
 */
@ServiceScope
@Component(dependencies = AppComponent.class, modules = ServiceModule.class)
public interface ServiceComponent {
    Service getService();

    void inject(DownloadService downloadService);
}
