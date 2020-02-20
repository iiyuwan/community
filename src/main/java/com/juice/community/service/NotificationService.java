package com.juice.community.service;

import com.juice.community.dto.NotificationDTO;
import com.juice.community.dto.PageDTO;
import com.juice.community.dto.QuestionDTO;
import com.juice.community.enums.NotificationStatusEnum;
import com.juice.community.enums.NotificationTypeEnum;
import com.juice.community.exception.CustomException;
import com.juice.community.exception.ECustomErrorCode;
import com.juice.community.mapper.NotificationMapper;
import com.juice.community.mapper.UserMapper;
import com.juice.community.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class NotificationService {
    @Autowired(required = false)
    private NotificationMapper notificationMapper;
    @Autowired(required = false)
    private UserMapper userMapper;
    public PageDTO list(Long userId, Integer page, Integer size) {

        PageDTO<NotificationDTO> pageDTO=new PageDTO<>();
        NotificationExample example=new NotificationExample();
        example.createCriteria().andReceiverEqualTo(userId);
        Integer totalCount = (int)notificationMapper.countByExample(example);
        Integer totalPage=0;
        if(totalCount%size==0) totalPage=totalCount/size;
        else totalPage=totalCount/size+1;//计算他一共有多少页
        pageDTO.setPage(totalPage,page);//根据当前页码来设置要展示的页码
        Integer offset=size*(page-1);

        NotificationExample example1=new NotificationExample();
        example1.createCriteria().andReceiverEqualTo(userId);
        example1.setOrderByClause("gmt_create desc");
        System.out.println("执行了");
        List<Notification> notificationList= notificationMapper.selectByExampleWithRowbounds(example1, new RowBounds(offset, size));
        if(notificationList.size()==0){return pageDTO; }
        List<NotificationDTO>notificationDTOS=new ArrayList<>();
        for (Notification n : notificationList) {
            NotificationDTO dto=new NotificationDTO();
            BeanUtils.copyProperties(n,dto);
            dto.setTypeName(NotificationTypeEnum.nameOfType(n.getType()));
            notificationDTOS.add(dto);
        }
        pageDTO.setData(notificationDTOS);
        return pageDTO;
    }

    public Long unreadCount(Long userId) {
        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(userId).andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(example);
    }

    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if(notification==null){
             throw new CustomException(ECustomErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if(!notification.getReceiver().equals(user.getId())){
            throw new CustomException(ECustomErrorCode.READ_NOTIFICATION_FAIL);
        }

        notification.setStatus(NotificationStatusEnum.READ.getStatus());//设置为已读
        notificationMapper.updateByPrimaryKey(notification);
        NotificationDTO dto=new NotificationDTO();
        BeanUtils.copyProperties(notification,dto);
        dto.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return dto;
    }
}
