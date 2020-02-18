package com.juice.community.dto;

import com.juice.community.exception.CustomException;
import com.juice.community.exception.ECustomErrorCode;
import lombok.Data;

@Data
public class ResultDTO<T> {
    private Integer code;//状态码
    private String message;
    private T data;
    public static ResultDTO causeBy(Integer code,String message){
        ResultDTO resultDTO=new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }
    public static ResultDTO causeBy(ECustomErrorCode errorCode){
        return causeBy(errorCode.getCode(),errorCode.getMessage());
    }
    public static ResultDTO successOf(){//请求成功
        ResultDTO resultDTO=new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        return resultDTO;
    }
    public static <T> ResultDTO successOf(T data){//请求成功
        ResultDTO resultDTO=new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        resultDTO.setData(data);
        return resultDTO;
    }
    public static ResultDTO causeBy(CustomException e){
        return causeBy(e.getCode(),e.getMessage());
    }
}
