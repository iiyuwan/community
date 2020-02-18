package com.juice.community.mapper;

import com.juice.community.model.Comment;
import com.juice.community.model.CommentExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface CommentHelperMapper {
   void addCommentCount(Comment comment);
}