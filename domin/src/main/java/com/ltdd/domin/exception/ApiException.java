package com.ltdd.domin.exception;


/**
 * @author lpy
 * @date 2019/2/22 11:11
 * @description 数据异常处理
 */
public class ApiException extends RuntimeException {

    public int code;

    public ApiException(int code, String message) {
        super(message);
        this.code = code;
    }
}
