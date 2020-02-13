package com.juice.community.mapper;

import com.juice.community.model.Question;
import com.juice.community.model.QuestionExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface QuestionHelperMapper {

    int addReviewCount(Question record);
}