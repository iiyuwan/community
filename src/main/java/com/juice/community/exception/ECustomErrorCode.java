package com.juice.community.exception;

public enum ECustomErrorCode implements ICustomErrorCode {

    QUESTION_NOT_FOUND("您访问的问题不存在或已被删除",404),
    COMMENT_TYPE_ERROR("评论类型错误或不存在",405),
    TARGET_PARAM_NOT_FOUND("未选择任何问题或者评论进行回复",406),
    COMMENT_NOT_LOGIN("您还未登陆，请登录后再评论",407),
    SYSTEM_BUSY("服务器冒烟啦，请少侠留步",505),
    COMMENT_NOT_FOUND("您操作的评论不存在",408),
    CONTENT_IS_EMPTY("输入内容不能为空",409);
    private String message;
    private Integer code;

    ECustomErrorCode(String message){
        this.message=message;
    }
    ECustomErrorCode(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage(){
        return message;
    }
    public Integer getCode(){
        return code;
    }
}
