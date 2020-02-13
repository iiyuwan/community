package com.juice.community.exception;

public enum ECustomErrorCode implements ICustomErrorCode {

    QUESTION_NOT_FOUND("您访问的问题不存在或已被删除");
    private String message;
    ECustomErrorCode(String message){
        this.message=message;
    }
    public String getMessage(){
        return message;
    }
}
