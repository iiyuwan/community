package com.juice.community.controller;
import com.juice.community.dto.PageDTO;
import com.juice.community.model.User;
import com.juice.community.service.NotificationService;
import com.juice.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    @Autowired(required = false)
    private QuestionService questionService;
    @Autowired(required = false)
    private NotificationService notificationService;
    @GetMapping("/profile/{action}") /*访问profile会调用此方法*/
    public String profile(@PathVariable(name="action") String action, Model model, HttpServletRequest request,
                          @RequestParam(value = "page",defaultValue = "1")Integer page, //当前页码
                          @RequestParam(value = "size",defaultValue = "4")Integer size){
        User user =(User)request.getSession().getAttribute("user");
        if(user==null) {
            model.addAttribute("error","用户未登录");
            return "redirect:/"; //未登录就返回首页
        }


        if("questions".equals(action)){
            PageDTO pageDTO = questionService.getQuestionListByUser(user.getId(), page, size);//根据用户的id查找他发布了多少问题
            model.addAttribute("pages",pageDTO);//添加至页面的model中，那边调用就可
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的问题");

        }else if("replies".equals(action)){
            PageDTO pageDTO=notificationService.list(user.getId(),page,size);
            Long unreadCount=notificationService.unreadCount(user.getId());
            model.addAttribute("pages",pageDTO);//添加至页面的model中，那边调用就可
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
            model.addAttribute("unreadCount",unreadCount );

        }
        return "profile";
    }
}
