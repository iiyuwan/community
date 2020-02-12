package com.juice.community.mapper;

import com.juice.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {
    @Insert("insert into question(id,title,description,gmt_create,gmt_modified," +
            "creator,tag)values(#{id},#{title},#{description},#{gmt_create},#{gmt_modified},#{creator},#{tag})")
    void create(Question question);
    @Select("select *from question limit #{offset},#{size}")
    List<Question> getQuestionList(@Param(value = "offset") Integer offset,@Param(value = "size") Integer size);
    @Select("select count(1) from question")
    Integer count();
    @Select("select *from question where creator=#{userId} limit #{offset},#{size}")
    List<Question> getQuestionListByUser(@Param("userId") Integer userId,@Param(value = "offset")  Integer offset, Integer size);
    @Select("select count(1) from question where creator=#{userId}")
    Integer countByUser(@Param("userId") Integer userId);//查询该用户发布的问题
    @Select("select *from question where id=#{id}")
    Question getQuestionById(@Param("id") Integer id);//根据id返回这个问题
}
