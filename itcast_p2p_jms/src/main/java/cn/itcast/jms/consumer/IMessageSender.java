package cn.itcast.jms.consumer;

import org.springframework.stereotype.Component;

import javax.jms.MapMessage;


public interface IMessageSender {
    public void sendMessage(MapMessage mapMessage);
}
