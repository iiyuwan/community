package com.juice.community.dto;

import lombok.Data;

import java.util.List;

@Data
public class TagDTO {
    private String tabName;//标签面板名字
    private List<String> tags;
}
