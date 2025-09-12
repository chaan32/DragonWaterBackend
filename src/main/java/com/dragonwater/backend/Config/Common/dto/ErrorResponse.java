package com.dragonwater.backend.Config.Common.dto;

import com.dragonwater.backend.Config.Exception.New.ErrorCode;

import java.time.LocalDateTime;

public class ErrorResponse {
    private final boolean success = false;
    private final String code;
    private String message;
    private final String timestamp;
    private final String path;
    private final Integer status;

    public ErrorResponse(ErrorCode errorCode, String path) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.status = errorCode.getHttpStatus();
        this.timestamp = LocalDateTime.now().toString();
        this.path = path;
    }

    public ErrorResponse(ErrorCode errorCode, String customMessage, String path) {
        this.code = errorCode.getCode();
        this.message = customMessage;
        this.timestamp = LocalDateTime.now().toString();
        this.path = path;
        this.status = errorCode.getHttpStatus();
    }

    // getters
    public boolean isSuccess() { return success; }
    public String getCode() { return code; }
    public String getMessage() { return message; }
    public String getTimestamp() { return timestamp; }
    public String getPath() { return path; }
    public Integer getStatus() {return status;}
    public void setMessage(String message){
        this.message = message;
    }
}
