package com.ltdd.presentation.util;

import android.app.Activity;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.StringRes;

/**
 * @author lpy
 * @date 2019/2/22 15:12
 * @description
 */

public class SnackbarUtil {

    private SnackbarUtil() {
    }

    public static void show(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }

    public static void show(View view, @StringRes int msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }

    public static void show(Activity activity, String msg) {
        Snackbar.make(activity.findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT).show();
    }

    public static void showLong(Activity activity, String msg) {
        Snackbar.make(activity.findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show();
    }

    public static void show(Activity activity, @StringRes int msg) {
        Snackbar.make(activity.findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT).show();
    }
}
