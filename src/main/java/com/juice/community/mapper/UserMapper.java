package com.juice.community.mapper;

import com.juice.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    //自动会去插入 根据下面user变量的名字
    @Insert("insert into user(account_id,name,state,gmt_create,gmt_modified) values(#{name},#{account_id},#{state},#{gmt_create},#{gmt_modified})")
    void insert(User user);
}
