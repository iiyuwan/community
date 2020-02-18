package com.juice.community.mapper;

import com.juice.community.model.Question;
import com.juice.community.model.QuestionExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface QuestionHelperMapper {

    void addReviewCount(Question record);
    void addCommentCount(Question question);//对某个问题增加回复数
    List<Question> selectRelatedQuestion(Question question);//模糊搜索 含有tag的相关题目
}