package com.juice.community.service;

import com.juice.community.dto.CommentGetDTO;
import com.juice.community.enums.CommentTypeEnum;
import com.juice.community.enums.NotificationStatusEnum;
import com.juice.community.enums.NotificationTypeEnum;
import com.juice.community.exception.CustomException;
import com.juice.community.exception.ECustomErrorCode;
import com.juice.community.mapper.*;
import com.juice.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired(required = false)
    private CommentMapper commentMapper;
    @Autowired(required = false)
    private QuestionMapper questionMapper;
    @Autowired(required = false)
    private QuestionHelperMapper questionHelperMapper;
    @Autowired(required = false)
    private UserMapper userMapper;
    @Autowired(required = false)
    private CommentHelperMapper commentHelperMapper;
    @Autowired(required = false)
    private NotificationMapper notificationMapper;

    //添加评论
    @Transactional //添加事务
    public void insert(Comment comment, User commentator) {
        if (comment.getParent_id() == null || comment.getParent_id() == 0) {
            throw new CustomException(ECustomErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomException(ECustomErrorCode.COMMENT_TYPE_ERROR);
        }
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParent_id());
            if (dbComment == null) {
                throw new CustomException(ECustomErrorCode.COMMENT_NOT_FOUND);
            }
            Question question = questionMapper.selectByPrimaryKey(dbComment.getParent_id());
            if (question == null) {
                throw new CustomException(ECustomErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);//插入评论 --一级评论
            //评论数加一
            Comment commentParent = new Comment();
            commentParent.setId(comment.getParent_id());
            commentParent.setComment_count(1L);
            commentHelperMapper.addCommentCount(commentParent);

            //设置通知
            createNotify(comment, dbComment.getCommentator(),commentator.getName(),question.getTitle(),question.getId(),NotificationTypeEnum.REPLY_COMMENT);
        } else {
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParent_id());
            if (question == null) {
                throw new CustomException(ECustomErrorCode.QUESTION_NOT_FOUND);
            }
            question.setComment_count(0);
            commentMapper.insert(comment);
            question.setComment_count(1);
            questionHelperMapper.addCommentCount(question);
            //回复了评论 也创建通知
            createNotify(comment,question.getCreator(),commentator.getName(),question.getTitle(),question.getId(),NotificationTypeEnum.REPLY_QUESTION);
        }
    }

    private void createNotify(Comment comment, Long receiver,String notifierName,String outerTitle,Long outerId,NotificationTypeEnum typeEnum) {
        if(receiver==comment.getCommentator()){
            return;
        }

        Notification notification=new Notification();
        notification.setGmt_create(System.currentTimeMillis());
        notification.setType(typeEnum.getType());
        notification.setOuter_id(outerId);
        notification.setNotifier(comment.getCommentator());//设置发送方
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);//设置接收方
        notification.setNotifier_name(notifierName);
        notification.setOuter_title(outerTitle);
        notificationMapper.insert(notification);
    }


    public List<CommentGetDTO> listByTargetId(Long id, CommentTypeEnum type) {
        CommentExample example = new CommentExample();
        example.createCriteria()
                .andParent_idEqualTo(id)
                .andTypeEqualTo(type.getType());
        example.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(example);
        if (comments.size() == 0) return new ArrayList<>();
        //得到所有的评论者 去重
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList<>();
        userIds.addAll(commentators);
        //获取评论人并转换为map
        UserExample example1 = new UserExample();
        example1.createCriteria().andIdIn(userIds);
        List<User> users = userMapper.selectByExample(example1);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));
        //转换comment为commentgetdto
        List<CommentGetDTO> commentGetDTOS = comments.stream().map(comment -> {
            CommentGetDTO commentGetDTO = new CommentGetDTO();
            BeanUtils.copyProperties(comment, commentGetDTO);
            commentGetDTO.setUser(userMap.get(comment.getCommentator()));
            return commentGetDTO;
        }).collect(Collectors.toList());
        return commentGetDTOS;
    }
}
