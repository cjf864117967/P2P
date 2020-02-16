package cn.itcast.service.message.impl;

import cn.itcast.service.message.EmailMessageService;
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
public class EmailMessageServiceimpl implements EmailMessageService {

    @Resource
    JmsTemplate jmsTemplate;

    @Override
    public void sendMail(String title, String to, String content) {
        this.sendMail(title , to ,  null  , content );
    }

    @Override
    public void sendMail(String title, List<String> to, String content) {
        this.sendMail(title, to, null, content);
    }

    @Override
    public void sendMail(String title, String to, String cc, String content) {
        this.sendMail(title, Arrays.asList(to), Arrays.asList(cc), content);
    }
    @Override
    public void sendMail(String title, List<String> to, List<String> cc, String content) {
        if(null == to || 0 == to.size() || StringUtils.isEmpty(title)){
//            log.error("消息发送：：发送短信：：参数错误");
            return;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(MessageType, EmailMessage);
        map.put(EmailMessageTo, to);
        map.put(EmailMessageCC, cc);
        map.put(EmailMessageTitle, title);
        map.put(MessageContent, content);

//        log.debug("消息发送：：发送短信：：开始发送消息");
//        log.debug(to);
//        log.debug(content);
        jmsTemplate.convertAndSend(map);

    }
}
