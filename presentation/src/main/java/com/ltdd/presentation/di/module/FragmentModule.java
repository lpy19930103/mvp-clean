package com.ltdd.presentation.di.module;

import android.app.Activity;



import com.ltdd.presentation.di.scope.FragmentScope;

import androidx.fragment.app.Fragment;
import dagger.Module;
import dagger.Provides;

/**
 * @author lpy
 * @date 2019/2/22 13:47
 * @description
 */
@Module
public class FragmentModule {

    private Fragment fragment;

    public FragmentModule(Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @FragmentScope
    Activity provideActivity() {
        return fragment.getActivity();
    }
}
