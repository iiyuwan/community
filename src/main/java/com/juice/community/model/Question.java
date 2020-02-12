package com.juice.community.model;

import lombok.Data;

@Data
public class Question {
    private int id;
    private String title;
    private String description;
    private long gmt_create;
    private long gmt_modified;
    private int creator;//关联的是user的id 然后拿到头像
    private int comment_count;
    private int review_count;
    private int like_count;
    private String tag;
}
