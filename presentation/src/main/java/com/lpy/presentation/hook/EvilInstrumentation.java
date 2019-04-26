package com.lpy.presentation.hook;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.lpy.presentation.view.activity.HookActivity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


//Instrumentation 代理对象
public class EvilInstrumentation extends Instrumentation {
    private static final String TAG = "EvilInstrumentation";
    
    // ActivityThread中原始的对象, 保存起来
    Instrumentation mBase;
    
    public EvilInstrumentation(Instrumentation mBase) {
        this.mBase = mBase;
    }
    
    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, Activity target,
            Intent intent, int requestCode, Bundle options) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        intent.setClass(who, HookActivity.class);
        // Hook之前, XXX到此一游!
        Log.d(TAG, "\n执行了startActivity, 参数如下: \n" + "who = [" + who + "], " +
                "\ncontextThread = [" + contextThread + "], \ntoken = [" + token + "], " +
                "\ntarget = [" + target + "], \nintent = [" + intent +
                "], \nrequestCode = [" + requestCode + "], \noptions = [" + options + "]");
        Method execStartActivity = Instrumentation.class.getDeclaredMethod("execStartActivity", Context.class, IBinder.class, IBinder.class, Activity.class,
                Intent.class, int.class, Bundle.class);
        execStartActivity.setAccessible(true);
        return (ActivityResult) execStartActivity.invoke(mBase, who,
                contextThread, token, target, intent, requestCode, options);
    }
    
}
