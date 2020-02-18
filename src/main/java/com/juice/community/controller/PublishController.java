package com.juice.community.controller;
import com.juice.community.cache.TagCache;
import com.juice.community.dto.QuestionDTO;
import com.juice.community.mapper.QuestionMapper;
import com.juice.community.model.Question;
import com.juice.community.model.User;
import com.juice.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import java.io.Console;

@Controller
public class PublishController {
    @Autowired(required = false)
    private QuestionService questionService;
    @GetMapping("/publish")
    public String publish(Model model){
        model.addAttribute("tags", TagCache.getTags());
        return "publish";
    }

    @GetMapping("/publish/{id}")//点击编辑->传入问题id->查询
    public String edit(@PathVariable(name = "id") Long id,Model model){//编辑
        QuestionDTO question = questionService.getQuestionById(id);
        model.addAttribute("title",question.getTitle());//传question过去 在publish里展示
        model.addAttribute("tag",question.getTag());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("id",question.getId());
        model.addAttribute("tags", TagCache.getTags());
        return "publish";
    }

    @PostMapping("/publish") /*发布问题 调用此处*/
    public String doPublish(
            @RequestParam(value = "title",required = false) String title,
            @RequestParam(value = "description",required = false) String description,
            @RequestParam(value = "tag",required = false) String tag,
            @RequestParam(value = "id",required = false) Long id,
            HttpServletRequest request,
            Model model) {
        model.addAttribute("tag",tag);
        model.addAttribute("description",description);
        model.addAttribute("title", title);
        model.addAttribute("tags", TagCache.getTags());
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "用户未登陆");
            model.addAttribute("tags", TagCache.getTags());
            return "publish";
        }
        if(StringUtils.isBlank(title)){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if(StringUtils.isBlank(description)){
            model.addAttribute("error","内容不能为空");
            return "publish";
        }
        String invalid = TagCache.filterInvalid(tag);
        if(StringUtils.isNoneBlank(invalid)){
            model.addAttribute("error","输入非法标签："+invalid);
            return "publish";
        }
        if(StringUtils.isBlank(tag)){
            model.addAttribute("error","您还没选择标签");
            return "publish";
        }
            Question question = new Question();
            question.setId(id);
            question.setTitle(title);
            question.setDescription(description);
            question.setTag(tag);
            question.setGmt_create(System.currentTimeMillis());
            question.setGmt_modified(question.getGmt_create());
            question.setCreator(user.getId());
            //此处也应该是createOrUpdate
            questionService.createOrUpdate(question);
            return "redirect:/";

    }
}
