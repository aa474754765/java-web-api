package com.kazibu.system.entity;
import com.kazibu.system.enumData.ErrorCode;

public class Result<T> {
    private String errorCode;
    private String errorMsg;
    private T data;

    public Result() {}

    public Result(String errorCode, String errorMsg, T data) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ErrorCode.SUCCESS.getCode(), "success", data);
    }

    public static <T> Result<T> error(String errorCode, String errorMsg) {
        return new Result<>(errorCode, errorMsg, null);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
