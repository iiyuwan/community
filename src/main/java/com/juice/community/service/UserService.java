package com.juice.community.service;

import com.juice.community.mapper.UserMapper;
import com.juice.community.model.User;
import com.juice.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
     @Autowired(required = false)
    private UserMapper userMapper;
    //如果登录的accountID等于数据库中的accountID就更新token 若没有就插入
    public void createOrUpdate(User user) {
      //User dbUser = userMapper.findByAccountId(user.getAccount_id());
        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccount_idEqualTo(user.getAccount_id());
        List<User> users = userMapper.selectByExample(userExample);
        if(users.size()==0){
          //插入user
          user.setGmt_create(System.currentTimeMillis());
          user.setGmt_modified(user.getGmt_create());
          userMapper.insert(user);
      }else{
            User dbUser = users.get(0);
            User updateUser=new User();
            updateUser.setGmt_modified(System.currentTimeMillis());//更新修改时间
            updateUser.setAvatar_url(user.getAvatar_url());//更新头像
            updateUser.setName(user.getName());
            updateUser.setToken(user.getToken());
            UserExample example=new UserExample();
            example.createCriteria().andIdEqualTo(dbUser.getId());
          //更新token
          userMapper.updateByExampleSelective(updateUser,example);
      }
    }
}

