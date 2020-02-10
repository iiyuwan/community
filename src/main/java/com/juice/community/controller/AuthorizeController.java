package com.juice.community.controller;

import com.juice.community.dto.AccessTokenDTO;
import com.juice.community.dto.GithubUser;
import com.juice.community.mapper.UserMapper;
import com.juice.community.model.User;
import com.juice.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired(required = false)//默认注入的时候bean必须存在
    private UserMapper userMapper;
    @Autowired
    private GithubProvider githubProvider;
    @Value("${github.client.id}")//根据配置文件读取数据
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;
    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletRequest request) throws IOException {
        AccessTokenDTO accessTokenDTO=new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if(githubUser!=null){//登陆成功 存入cookie和session
           request.getSession().setAttribute("user",githubUser);

            User user = new User();
            user.setState("xxxxxxxx");
            user.setName(githubUser.getName());
            user.setAccount_id(String.valueOf(githubUser.getId()));
            user.setGmt_create(System.currentTimeMillis());
            user.setGmt_modified(user.getGmt_create());
            userMapper.insert(user);
            return "redirect:/";//登录成功跳转到index界面
        }else{
            //登录失败
            return "redirect:/";//返回index界面
        }

    }
}