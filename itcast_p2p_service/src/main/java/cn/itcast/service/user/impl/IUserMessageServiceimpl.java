package cn.itcast.service.user.impl;

import cn.itcast.dao.IUserMessageDAO;
import cn.itcast.domain.userMessage.UserMessage;
import cn.itcast.service.user.IUserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class IUserMessageServiceimpl implements IUserMessageService {
    @Autowired
    IUserMessageDAO iUserMessageDAO;
    @Override
    public void insertMessages(String userId, String messageContent) {



        UserMessage message = new UserMessage();
        message.setS_receive_user_id(Integer.valueOf(userId));  //接受用户ID
        message.setMessageContent(messageContent); //信息内容
        message.setMessageState(0); //未读信息
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hm:ss");
        message.setSendTime(sdf.format(new Date())); //发送时间
        iUserMessageDAO.save(message);
    }
}
