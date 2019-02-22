package com.ltdd.presentation.di.component;


import android.content.Context;


import com.ltdd.domin.repository.LoginRepository;
import com.ltdd.presentation.di.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;

/**
 * @author lpy
 * @date 2019/2/22 13:50
 * @description 
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    Context provideContext();

    OkHttpClient provideOkhttpClient();

    LoginRepository provideLoginRepository();

}
