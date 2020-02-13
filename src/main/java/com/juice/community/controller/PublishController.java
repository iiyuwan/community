package com.juice.community.controller;
import com.juice.community.dto.QuestionDTO;
import com.juice.community.mapper.QuestionMapper;
import com.juice.community.model.Question;
import com.juice.community.model.User;
import com.juice.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
@Controller
public class PublishController {
    @Autowired(required = false)
    private QuestionService questionService;
    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }
    @GetMapping("/publish/{id}")//点击页面后获取到id
    public String edit(@PathVariable(name = "id") Integer id,Model model){//编辑
        QuestionDTO question = questionService.getQuestionById(id);
        model.addAttribute("question",question);//传question过去 在publish里展示
        return "publish";
    }

    @PostMapping("/publish") /*此注解就是接受post请求 并得到表单元素进行验证*/
    public String doPublish(
            @RequestParam(value = "title",required = false) String title,
            @RequestParam(value = "description",required = false) String description,
            @RequestParam(value = "tag",required = false) String tag,
            @RequestParam(value = "id",required = false) Integer id,
            HttpServletRequest request,
            Model model) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "用户未登陆");
            return "publish";
        }
            Question question = new Question();
            question.setId(id);
            question.setTitle(title);
            question.setDescription(description);
            question.setTag(tag);
            question.setCreator(user.getId());

            //此处也应该是createOrUpdate
            questionService.createOrUpdate(question);

            return "redirect:/";

    }
}
