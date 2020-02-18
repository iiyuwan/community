package com.juice.community.advice;

import com.alibaba.fastjson.JSON;
import com.juice.community.dto.ResultDTO;
import com.juice.community.exception.CustomException;
import com.juice.community.exception.ECustomErrorCode;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    ModelAndView handle(HttpServletRequest request, HttpServletResponse response,
                  Throwable ex, Model model)  {
        String contentType = request.getContentType();
        if("application/json".equals(contentType)){
            //返回json格式
            ResultDTO resultDTO=null;
            if(ex instanceof CustomException){
              resultDTO= ResultDTO.causeBy((CustomException) ex);
            }else {
                resultDTO=  ResultDTO.causeBy(ECustomErrorCode.SYSTEM_BUSY);
            }
            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            }catch (IOException e){

            }
          return null;

        }else{
            //返回错误页面跳转
            if(ex instanceof CustomException){
                model.addAttribute("errorMessage",ex.getMessage());
            }else {
                model.addAttribute("errorMessage",ECustomErrorCode.SYSTEM_BUSY.getMessage());
            }

            return new ModelAndView("error");//返回到error页面
        }

    }

}
