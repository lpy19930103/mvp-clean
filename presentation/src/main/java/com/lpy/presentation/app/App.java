package com.lpy.presentation.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;


import com.lpy.presentation.injector.component.AppComponent;
import com.lpy.presentation.injector.component.DaggerAppComponent;
import com.lpy.presentation.injector.module.AppModule;

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
  

    public static synchronized App getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (isMainProcess()) {
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
