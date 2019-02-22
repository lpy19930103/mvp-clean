package com.ltdd.domin.modules;

public class BasicResponse<T> {
    public T data;
    public int code;
    public String msg;
    
    public BasicResponse() {
    }
    
    public BasicResponse(int code, String msg) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("error = " + code).append(" ");
        sb.append("desc = " + msg).append(" ");
        return sb.toString();
    }


}
