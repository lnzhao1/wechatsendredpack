package com.wxpay.paydemo.model;

/**
 * @author zhaoxiao 2020/1/8
 */
public class Result {

    private boolean status;

    private String statusCode;

    private String msg;

    public Result(boolean status, String statusCode, String msg) {
        this.status = status;
        this.statusCode = statusCode;
        this.msg = msg;
    }

    public Result() {
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
