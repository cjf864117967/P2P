package cn.itcast.jms.consumer.impl;

import cn.itcast.jms.consumer.IMessageSender;
import org.springframework.stereotype.Component;

import javax.jms.MapMessage;

@Component("msmSender")
public class MSMMessageSender implements IMessageSender {
    @Override
    public void sendMessage(MapMessage mapMessage) {
        System.out.println("发送短信......");
    }
}
