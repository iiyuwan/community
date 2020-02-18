package com.juice.community.controller;
import com.juice.community.dto.CommentDTO;
import com.juice.community.dto.CommentGetDTO;
import com.juice.community.dto.QuestionDTO;
import com.juice.community.enums.CommentTypeEnum;
import com.juice.community.service.CommentService;
import com.juice.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {
    @Autowired(required = false)
    private QuestionService questionService;
    @Autowired(required = false)
    private CommentService commentService;
    @GetMapping("/question/{id}")
    public String question(@PathVariable(name="id") Long id, Model model){
        //得到id后，根据id拿到相应问题的内容信息，再加载到页面上
        QuestionDTO questionDTO=questionService.getQuestionById(id);
        List<QuestionDTO> relatedQuestion=questionService.selectRelatedQuestion(questionDTO);
        List<CommentGetDTO>commentDTOS =commentService.listByTargetId(id, CommentTypeEnum.QUESTION);//根据问题id，找出其所有评论
        model.addAttribute("question",questionDTO);//通过这个写到页面上
        model.addAttribute("comments",commentDTOS);
        model.addAttribute("relatedQuestions",relatedQuestion);//得到相关问题
        questionService.addReviewCount(id);//增加阅读数



        return "question";
    }

}
