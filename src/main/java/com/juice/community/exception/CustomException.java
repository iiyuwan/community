package com.juice.community.exception;

public class CustomException extends RuntimeException {
    private String message;
    private Integer code;
    public CustomException(ICustomErrorCode errorCode ){

        this.message=errorCode.getMessage();
        this.code=errorCode.getCode();
    }
    @Override
    public String getMessage(){
        return message;
    }
    public Integer getCode() {
        return code;
    }
}
