package com.ltdd.presentation.di.component;

import android.app.Service;


import com.ltdd.data.service.DownloadService;
import com.ltdd.presentation.di.module.ServiceModule;
import com.ltdd.presentation.di.scope.ServiceScope;

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
