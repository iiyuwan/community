package com.juice.community.controller;

import com.juice.community.dto.QuestionDTO;
import com.juice.community.mapper.QuestionMapper;
import com.juice.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionController {
    @Autowired(required = false)
    private QuestionService questionService;
    @GetMapping("/question/{id}")
    public String question(@PathVariable(name="id") Integer id, Model model){
        //得到id后，根据id拿到相应问题的内容信息，再加载到页面上
        QuestionDTO questionDTO=questionService.getQuestionById(id);
        model.addAttribute("question",questionDTO);//通过这个写到页面上
        return "question";
    }
}
