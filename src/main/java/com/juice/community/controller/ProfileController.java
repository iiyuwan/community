package com.juice.community.controller;
import com.juice.community.dto.PageDTO;
import com.juice.community.model.User;
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
    @Autowired
    private QuestionService questionService;
    @GetMapping("/profile/{action}") /*访问profile会调用此方法*/
    public String profile(@PathVariable(name="action") String action, Model model, HttpServletRequest request,
                          @RequestParam(value = "page",defaultValue = "1")Integer page, //当前页码
                          @RequestParam(value = "size",defaultValue = "4")Integer size){
        User user =(User)request.getSession().getAttribute("user");
        if(user==null) {
            model.addAttribute("error","用户未登录");
            return "redirect:/"; //未登录就返回首页
        }

        PageDTO pageDTO = questionService.getQuestionListByUser(user.getId(), page, size);//根据用户的id查找他发布了多少问题
        model.addAttribute("pages",pageDTO);//添加至页面的model中，那边调用就可
        if("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的问题");
        }else if("replies".equals(action)){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
        }
        return "profile";
    }
}
