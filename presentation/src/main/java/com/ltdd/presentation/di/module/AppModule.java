package com.ltdd.presentation.di.module;

import android.content.Context;


import com.ltdd.data.api.HttpHelper;
import com.ltdd.data.repository.LoginDataRepository;
import com.ltdd.domin.repository.LoginRepository;
import com.ltdd.presentation.app.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * @author lpy
 * @date 2019/2/22 12:07
 * @description
 */
@Module
public class AppModule {
    private final App application;

    public AppModule(App application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    OkHttpClient provideOkhttpClient(HttpHelper httpHelper) {
        return httpHelper.getOkHttpClient();
    }

    @Provides
    @Singleton
    LoginRepository provideLoginManager(LoginDataRepository loginDataRepository) {
        return loginDataRepository;
    }
}
