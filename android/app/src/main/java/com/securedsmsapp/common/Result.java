package com.securedsmsapp.common;

public class Result {
    public Result() {
    }

    public Result(int errorCode) {
        this.errorCode = errorCode;
    }

    public Result(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int errorCode;
    public String errorMessage;
}
