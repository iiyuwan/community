package com.juice.community.controller;

import com.juice.community.dto.AccessTokenDTO;
import com.juice.community.dto.GithubUser;
import com.juice.community.mapper.UserMapper;
import com.juice.community.model.User;
import com.juice.community.provider.GithubProvider;
import com.juice.community.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Controller
@Slf4j
public class AuthorizeController {
    @Autowired(required = false)
    private UserService userService;
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
                           HttpServletRequest request,
                           HttpServletResponse response) throws IOException {
        AccessTokenDTO accessTokenDTO=new AccessTokenDTO();
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if(githubUser!=null){//登陆成功 存入cookie和session
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccount_id(String.valueOf(githubUser.getId()));

            user.setAvatar_url(githubUser.getAvatar_url());
            userService.createOrUpdate(user);
            response.addCookie(new Cookie("token",token));
            return "redirect:/";//登录成功跳转到index界面
        }else{
            //登录失败
            log.error("callback get github error {}",githubUser);
            return "redirect:/";//返回index界面
        }

    }
    //退出登录
    @GetMapping("/logout")
      public String logout(HttpServletRequest request,HttpServletResponse response){
        request.getSession().removeAttribute("user");//移出session
        Cookie cookie=new Cookie("token",null);//删除cookie的方法就是新建一个同名的
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";//返回index界面
      }


}
