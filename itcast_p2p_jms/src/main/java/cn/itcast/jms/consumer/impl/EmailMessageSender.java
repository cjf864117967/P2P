package cn.itcast.jms.consumer.impl;

import cn.itcast.jms.consumer.IMessageSender;
import org.springframework.stereotype.Component;

import javax.jms.MapMessage;
//发送邮件的具体操作类
@Component("emailSender")
public class EmailMessageSender implements IMessageSender {
    @Override
    public void sendMessage(MapMessage mapMessage) {
        System.out.println("发送邮件......");
    }
}
