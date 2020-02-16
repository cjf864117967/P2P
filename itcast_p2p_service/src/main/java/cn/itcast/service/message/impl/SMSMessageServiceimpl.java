package cn.itcast.service.message.impl;

import cn.itcast.service.message.SMSMessageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.itcast.utils.IMessage.*;

@Service
public class SMSMessageServiceimpl implements SMSMessageService {

    @Resource
    JmsTemplate jmsTemplate;
    @Override
    public void sendSMS(String to, String content) {
        this.sendSMS(Arrays.asList(to), content);
    }



    @Override
    public void sendSMS(List<String> to, String content) {

        if(null == to || 0 == to.size() || StringUtils.isEmpty(content)){
//            log.error("消息发送：：发送短信：：参数错误");
            return;
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(MessageType, SMSMessage);
        map.put(SMSNumbers, to);
        map.put(MessageContent, content);
//        log.debug("消息发送：：发送短信：：开始发送消息");
//        log.debug(to);
//        log.debug(content);
        jmsTemplate.convertAndSend(map);
    }






}
