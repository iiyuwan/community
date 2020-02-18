package com.juice.community.controller;
import com.juice.community.dto.CommentDTO;
import com.juice.community.dto.CommentGetDTO;
import com.juice.community.dto.ResultDTO;
import com.juice.community.enums.CommentTypeEnum;
import com.juice.community.exception.ECustomErrorCode;
import com.juice.community.model.Comment;
import com.juice.community.model.User;
import com.juice.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {
    @Autowired(required = false)
    private CommentService commentService;
    @RequestMapping(value = "/comment", method= RequestMethod.POST)
    @ResponseBody
    public Object post(@RequestBody CommentDTO commentDTO, HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        System.out.println("user = " + user);
        if(user==null) {
          return ResultDTO.causeBy(ECustomErrorCode.COMMENT_NOT_LOGIN);

        }
        if(commentDTO==null|| StringUtils.isBlank(commentDTO.getContent())){
            return ResultDTO.causeBy(ECustomErrorCode.CONTENT_IS_EMPTY);
        }
        Comment comment=new Comment();
        comment.setParent_id(commentDTO.getParent_id());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setCommentator(user.getId());
        comment.setGmt_create(System.currentTimeMillis());
        comment.setGmt_modified(comment.getGmt_create());
        comment.setLike_count(0L);
        commentService.insert(comment);
        return ResultDTO.successOf();
    }
   //展示子评论
    @ResponseBody
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    public ResultDTO<List> showSubComments(@PathVariable(name="id") Long id){
        List<CommentGetDTO> commentGetDTOS = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.successOf(commentGetDTOS);
    }
}
