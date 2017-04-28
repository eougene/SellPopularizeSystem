package com.yd.org.sellpopularizesystem.javaBean;

/**
 * Created by hejin on 2017/3/16.
 */

public class ErrorBean extends Domine {
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private String code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String msg;

}
