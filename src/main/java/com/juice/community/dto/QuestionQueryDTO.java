package com.juice.community.dto;

import lombok.Data;

@Data
public class QuestionQueryDTO {
    private String keyStr;
    private Integer page;
    private Integer size;
}
