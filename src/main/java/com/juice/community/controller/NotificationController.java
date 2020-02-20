package com.juice.community.controller;

import com.juice.community.dto.NotificationDTO;
import com.juice.community.dto.PageDTO;
import com.juice.community.enums.NotificationTypeEnum;
import com.juice.community.model.User;
import com.juice.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {
    @Autowired(required = false)
    private NotificationService notificationService;
    @GetMapping("/notification/{id}")
    public String profile(@PathVariable(name="id") Long id,HttpServletRequest request){
        User user =(User)request.getSession().getAttribute("user");
        if(user==null) {
            return "redirect:/"; //未登录就返回首页
        }
        NotificationDTO dto= notificationService.read(id,user);
        if(NotificationTypeEnum.REPLY_COMMENT.getType()==dto.getType() ||NotificationTypeEnum.REPLY_QUESTION.getType()==dto.getType()){
            return "redirect:/question/"+dto.getOuter_id();
        }else {
            return "redirect:/";
        }

    }

}
