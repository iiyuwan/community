package com.juice.community.service;

import com.juice.community.dto.PageDTO;
import com.juice.community.dto.QuestionDTO;
import com.juice.community.exception.CustomException;
import com.juice.community.exception.ECustomErrorCode;
import com.juice.community.mapper.QuestionHelperMapper;
import com.juice.community.mapper.QuestionMapper;
import com.juice.community.mapper.UserMapper;
import com.juice.community.model.Question;
import com.juice.community.model.QuestionExample;
import com.juice.community.model.User;
import org.apache.ibatis.session.RowBounds;
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
    @Autowired(required = false)
    private QuestionHelperMapper questionHelperMapper;
    //查询所有人发布了的问题
    public PageDTO getQuestionList(Integer page, Integer size) {
        PageDTO pageDTO=new PageDTO();
        Integer totalCount = (int)questionMapper.countByExample(new QuestionExample());
        Integer totalPage=0;
        if(totalCount%size==0) totalPage=totalCount/size;
        else totalPage=totalCount/size+1;
        pageDTO.setPage(totalPage,page);
        Integer offset=size*(page-1);
       // List<Question> questionList = questionMapper.getQuestionList(offset,size);
        List<Question> questionList= questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(offset, size));
        List<QuestionDTO>questionDTOList=new ArrayList<>();
        for (Question question : questionList) {
         User user=  userMapper.selectByPrimaryKey(question.getCreator());
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
        QuestionExample example=new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int)questionMapper.countByExample(example);
        Integer totalPage=0;
        if(totalCount%size==0) totalPage=totalCount/size;
        else totalPage=totalCount/size+1;//计算他一共有多少页
        pageDTO.setPage(totalPage,page);//根据当前页码来设置要展示的页码
        Integer offset=size*(page-1);
       // List<Question> questionList = questionMapper.getQuestionListByUser(userId,offset,size);
        QuestionExample example2=new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        List<Question> questionList= questionMapper.selectByExampleWithRowbounds(example2, new RowBounds(offset, size));
        List<QuestionDTO>questionDTOList=new ArrayList<>();
        for (Question question : questionList) {
            User user=  userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);//把question的所有属性拷贝到questionDTO
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        pageDTO.setQuestionDTOList(questionDTOList);
        return pageDTO;
    }

    public QuestionDTO getQuestionById(Integer id) {
        Question question=questionMapper.selectByPrimaryKey(id);
        if(question==null){
            throw  new CustomException(ECustomErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO=new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user=userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if(question.getId()==null){
            question.setGmt_modified(question.getGmt_create());
            questionMapper.insert(question);
        }else {
            Question updateQues=new Question();
            updateQues.setGmt_modified(System.currentTimeMillis());
            updateQues.setTitle(question.getTitle());
            updateQues.setTag(question.getTag());
            updateQues.setDescription(question.getDescription());
            QuestionExample example = new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());
            int res = questionMapper.updateByExampleSelective(updateQues, example);
            if(res!=1){
                throw  new CustomException(ECustomErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void addReviewCount(Integer id) {

        Question updateQue=new Question();
         updateQue.setId(id);
         updateQue.setReview_count(1);
        questionHelperMapper.addReviewCount(updateQue);
    }
}
