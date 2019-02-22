package com.ltdd.presentation.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;


import com.ltdd.presentation.di.component.AppComponent;
import com.ltdd.presentation.di.component.DaggerActivityComponent;
import com.ltdd.presentation.di.component.DaggerAppComponent;
import com.ltdd.presentation.di.module.AppModule;

import java.util.HashSet;
import java.util.Set;


/**
 * @author lpy
 * @date 2019/2/22 13:52
 * @description
 */
public class App extends Application {

    private static App sInstance;
    public AppComponent mAppComponent;
    private Set<Activity> mActivities;
    /**
     * APP状态 0为正常初始化
     * -1为被强杀
     * 如果被强杀那么在主进程将被重新初始化为-1，所以必须重走流程经过Splash界面赋值为0
     */
    public static int sAppState;

    public static synchronized App getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (isMainProcess()) {
            sAppState = -1;
            getAppComponent();
        }
    }

 

    
  

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        sInstance = this;
//        MultiDex.install(this);
    }

    public void addActivity(Activity activity) {
        if (mActivities == null) {
            mActivities = new HashSet<>();
        }
        mActivities.add(activity);
    }

    public void removeActivity(Activity activity) {
        if (mActivities != null) {
            mActivities.remove(activity);
        }
    }

    /**
     * 退出app
     */
    public void exitApp() {
        if (mActivities != null) {
            synchronized (this) {
                for (Activity activity : mActivities) {
                    activity.finish();
                }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * 获取当前进程名
     */
    private String getCurrentProcessName() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) {
            return null;
        }
        int pid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo process : activityManager.getRunningAppProcesses()) {
            if (process.pid == pid) {
                return process.processName;
            }
        }
        return null;
    }

    /**
     * 判断是否为主进程
     *
     * @return boolean
     */
    private boolean isMainProcess() {
        return getPackageName().equals(getCurrentProcessName());
    }

    public AppComponent getAppComponent() {
        if (mAppComponent == null) {
            mAppComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(sInstance))
                    .build();
        }
        return mAppComponent;
    }
}
