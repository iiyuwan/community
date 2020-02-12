package com.juice.community.service;

import com.juice.community.dto.PageDTO;
import com.juice.community.dto.QuestionDTO;
import com.juice.community.mapper.QuestionMapper;
import com.juice.community.mapper.UserMapper;
import com.juice.community.model.Question;
import com.juice.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired(required = false)
    private UserMapper userMapper;
    @Autowired(required = false)
    private QuestionMapper questionMapper;
    //查询所有人发布了的问题
    public PageDTO getQuestionList(Integer page, Integer size) {
        PageDTO pageDTO=new PageDTO();
        Integer totalCount = questionMapper.count();
        Integer totalPage=0;
        if(totalCount%size==0) totalPage=totalCount/size;
        else totalPage=totalCount/size+1;
        pageDTO.setPage(totalPage,page);
        Integer offset=size*(page-1);
        List<Question> questionList = questionMapper.getQuestionList(offset,size);
        List<QuestionDTO>questionDTOList=new ArrayList<>();
        for (Question question : questionList) {
         User user=  userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);//把question的所有属性拷贝到questionDTO
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        pageDTO.setQuestionDTOList(questionDTOList);
        return pageDTO;
    }
    //根据userID来查找他发表了多少问题
    public PageDTO getQuestionListByUser(int userId, Integer page, Integer size) {
        PageDTO pageDTO=new PageDTO();
        Integer totalCount = questionMapper.countByUser(userId);
        Integer totalPage=0;
        if(totalCount%size==0) totalPage=totalCount/size;
        else totalPage=totalCount/size+1;//计算他一共有多少页
        pageDTO.setPage(totalPage,page);//根据当前页码来设置要展示的页码
        Integer offset=size*(page-1);
        List<Question> questionList = questionMapper.getQuestionListByUser(userId,offset,size);
        List<QuestionDTO>questionDTOList=new ArrayList<>();

        for (Question question : questionList) {
            System.out.println("question = " + question);
            User user=  userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);//把question的所有属性拷贝到questionDTO
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        pageDTO.setQuestionDTOList(questionDTOList);
        return pageDTO;
    }

    public QuestionDTO getQuestionById(Integer id) {
        Question question=questionMapper.getQuestionById(id);
        QuestionDTO questionDTO=new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        return questionDTO;
    }
}
