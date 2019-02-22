package com.lpy.presentation.di.scope;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author lpy
 * @date 2019/2/22 12:06
 * @description
 */
@Scope
@Retention(RUNTIME)
public @interface AdapterScope {
}
