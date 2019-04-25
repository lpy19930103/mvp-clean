package com.lpy.presentation.hook;

import android.content.ClipData;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


//伪造Ibinder
public class BinderHookHandler implements InvocationHandler {
    private static final String TAG = "BinderHookHandler";
    // 原始的Service对象 (IInterface)
    Object base;
    
    public BinderHookHandler(IBinder base, Class<?> stubClass) {
        try {
            Method asInterfaceMethod = stubClass.getDeclaredMethod("asInterface", IBinder.class);
            this.base = asInterfaceMethod.invoke(null, base);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] objects) throws Throwable {
        // 把剪切版的内容替换为 "you are hooked"
        if ("getPrimaryClip".equals(method.getName())) {
            Log.d(TAG, "hook getPrimaryClip");
            return ClipData.newPlainText(null, "you are hooked");
        }
        
        // 欺骗系统,使之认为剪切版上一直有内容
        if ("hasPrimaryClip".equals(method.getName())) {
            return true;
        }
        
        return method.invoke(base, objects);
    }
}
