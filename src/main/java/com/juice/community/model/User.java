package com.juice.community.model;

import lombok.Data;

@Data  /*自动生成getter和setter方法*/
public class User {
    private int id;
    private String account_id;
    private String name;
    private String token;//token验证是否存在用户
    private long gmt_create;
    private long gmt_modified;
    private String avatar_url;
}
