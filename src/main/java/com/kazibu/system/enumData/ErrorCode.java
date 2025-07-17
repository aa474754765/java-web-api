package com.kazibu.system.enumData;

public enum ErrorCode {
    SUCCESS("0", "success"),
    USERNAME_OR_PASSWORD_ERROR("1001", "用户名或密码错误"),
    LOGIN_FAILED("1002", "登录失败，请稍后重试"),
    USERNAME_EXISTS("1003", "用户名已存在");

    private final String code;
    private final String msg;

    ErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
