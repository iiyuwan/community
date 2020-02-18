package com.juice.community.dto;

import com.juice.community.model.User;
import lombok.Data;

@Data
public class CommentGetDTO {
    //这个是从服务器拿到的commentDTO
    private Long id;
    private Long parent_id;
    private String content;
    private Integer type;
    private Long commentator;
    private Long gmt_create;
    private Long gmt_modified;
    private Long like_count;
    private Long comment_count;
    private User user;
}
