package com.yd.org.sellpopularizesystem.javaBean;

/**
 * Created by hejin on 2017/3/16.
 */

public class ErrorBean extends Domine {
    private String code;
    private String msg;

    private String trust_account_id;
    private String result;


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTrust_account_id() {
        return trust_account_id;
    }

    public void setTrust_account_id(String trust_account_id) {
        this.trust_account_id = trust_account_id;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}
