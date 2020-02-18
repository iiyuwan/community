package com.juice.community.dto;

import lombok.Data;

@Data
public class CommentDTO {
    //这个是从页面拿到的CommentDTO
    private Long parent_id;
    private String content;
    private Integer type;
}
