package com.example.designmode.retrofit;

//2.数据格式不一致 成功data是个对象 不成功data是个string
public class BaseResult {

    String code;
    String msg;

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

    public boolean isOk() {
        return "0000".equals(code);
    }
}
