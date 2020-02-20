package com.juice.community.dto;

import com.juice.community.model.User;
import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;
    private Long gmt_create;
    private Integer status;
    private String notifier_name;
    private Long outer_id;
    private String outer_title;//某某人   xx
    private String typeName;//点赞、评论、收藏
    private Integer type;
}
