package com.juice.community.controller;

import com.juice.community.dto.PageDTO;
import com.juice.community.dto.QuestionDTO;
import com.juice.community.mapper.QuestionMapper;
import com.juice.community.mapper.UserMapper;
import com.juice.community.model.Question;
import com.juice.community.model.User;
import com.juice.community.service.QuestionService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {
    @Autowired(required = false)
    private UserMapper userMapper;
    @Autowired(required = false)
    private QuestionService questionService;
    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(value = "page",defaultValue = "1")Integer page,
                        @RequestParam(value = "size",defaultValue = "5")Integer size){

        //展示用户问题
         PageDTO pageDTO=questionService.getQuestionList(page,size);
        model.addAttribute("pages",pageDTO);
        return "index";
    }
}
