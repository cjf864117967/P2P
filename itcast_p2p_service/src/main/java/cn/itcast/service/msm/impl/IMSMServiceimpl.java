package cn.itcast.service.msm.impl;

import cn.itcast.service.msm.IMSMService;
import cn.itcast.utils.IMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class IMSMServiceimpl implements IMSMService {

    @Autowired
    JmsTemplate jmsTemplate;
    //完成向mq发消息

    @Override
    public void sendMsm(String phone, String content) {
        Map<String, Object> map = new HashMap<>();
        map.put(IMessage.MessageType,IMessage.SMSMessage);//存储消息类型
        map.put(IMessage.MessageContent,content);

        map.put(IMessage.SMSNumbers,phone);



        jmsTemplate.convertAndSend(map);
    }
}
