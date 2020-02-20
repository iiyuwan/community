package com.juice.community.service;

import com.juice.community.dto.PageDTO;
import com.juice.community.dto.QuestionDTO;
import com.juice.community.dto.QuestionQueryDTO;
import com.juice.community.exception.CustomException;
import com.juice.community.exception.ECustomErrorCode;
import com.juice.community.mapper.QuestionHelperMapper;
import com.juice.community.mapper.QuestionMapper;
import com.juice.community.mapper.UserMapper;
import com.juice.community.model.Question;
import com.juice.community.model.QuestionExample;
import com.juice.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Autowired(required = false)
    private UserMapper userMapper;
    @Autowired(required = false)
    private QuestionMapper questionMapper;
    @Autowired(required = false)
    private QuestionHelperMapper questionHelperMapper;

    //查询所有人发布了的问题
    public PageDTO getQuestionList(String keyStr, Integer page, Integer size) {
        if(StringUtils.isNotBlank(keyStr)){
            String[] keywords = StringUtils.split(keyStr, " ");
            keyStr=Arrays.stream(keywords).collect(Collectors.joining("|"));
        }
        PageDTO<QuestionDTO> pageDTO=new PageDTO<>();

        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setKeyStr(keyStr);

        Integer totalCount = (int)questionHelperMapper.countByKeyWords(questionQueryDTO);
        Integer totalPage=0;
        if(totalCount%size==0) totalPage=totalCount/size;
        else totalPage=totalCount/size+1;
        pageDTO.setPage(totalPage,page);
        Integer offset=size*(page-1);
       // List<Question> questionList = questionMapper.getQuestionList(offset,size);
        QuestionExample example1=new QuestionExample();
        example1.setOrderByClause("gmt_create desc");
        questionQueryDTO.setPage(offset);
        questionQueryDTO.setSize(size);
        List<Question> questionList= questionHelperMapper.selectByKeyWords(questionQueryDTO);
        List<QuestionDTO>questionDTOList=new ArrayList<>();
        for (Question question : questionList) {
         User user=  userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);//把question的所有属性拷贝到questionDTO
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        pageDTO.setData(questionDTOList);
        return pageDTO;
    }
    //根据userID来查找他发表了多少问题
    public PageDTO getQuestionListByUser(Long userId, Integer page, Integer size) {
        PageDTO<QuestionDTO> pageDTO=new PageDTO<>();
        QuestionExample example=new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int)questionMapper.countByExample(example);
        Integer totalPage=0;
        if(totalCount%size==0) totalPage=totalCount/size;
        else totalPage=totalCount/size+1;//计算他一共有多少页
        pageDTO.setPage(totalPage,page);//根据当前页码来设置要展示的页码
        Integer offset=size*(page-1);

        example.createCriteria().andCreatorEqualTo(userId);
        QuestionExample example1=new QuestionExample();
        example1.setOrderByClause("gmt_create desc");
        List<Question> questionList= questionMapper.selectByExampleWithRowbounds(example1, new RowBounds(offset, size));
        List<QuestionDTO>questionDTOList=new ArrayList<>();
        for (Question question : questionList) {
            User user=  userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);//把question的所有属性拷贝到questionDTO
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        pageDTO.setData(questionDTOList);
        return pageDTO;
    }

    public QuestionDTO getQuestionById(Long id) {
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

            question.setGmt_create(System.currentTimeMillis());
            question.setGmt_modified(question.getGmt_create());
            question.setReview_count(0);
            question.setLike_count(0);
            question.setComment_count(0);
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

    public void addReviewCount(Long id) {

        Question updateQue=new Question();
         updateQue.setId(id);
         updateQue.setReview_count(1);
        questionHelperMapper.addReviewCount(updateQue);
    }

    public List<QuestionDTO> selectRelatedQuestion(QuestionDTO questionDTO) {
        if(StringUtils.isBlank(questionDTO.getTag())){
            //标签为空
            return new ArrayList<>();
        }else{
            String[] tags = StringUtils.split(questionDTO.getTag(), "-");//得到所有标签
            String regTag = Arrays.stream(tags).collect(Collectors.joining("|"));//按|拼接成正则表达式的格式：tag1|tag2|tag3
            Question question=new Question();
            question.setId(questionDTO.getId());
            question.setTag(regTag);
            List<Question> relatedQuestion = questionHelperMapper.selectRelatedQuestion(question);//得到相关问题
           List<QuestionDTO>relatedQuestionDTO= relatedQuestion.stream().map(q->{  //把question转换为questionＤＴＯ
               QuestionDTO temp = new QuestionDTO();
               BeanUtils.copyProperties(q,temp);
               return temp;
            }).collect(Collectors.toList());

            return relatedQuestionDTO;
        }

    }
}
