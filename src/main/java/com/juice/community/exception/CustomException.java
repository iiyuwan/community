package com.juice.community.exception;

public class CustomException extends RuntimeException {
    private String message;
    public CustomException(ICustomErrorCode errorCode ){

        this.message=errorCode.getMessage();
    }
    @Override
    public String getMessage(){
        return message;
    }
}
