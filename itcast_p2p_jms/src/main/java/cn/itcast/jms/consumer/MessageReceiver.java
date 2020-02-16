package cn.itcast.jms.consumer;

import cn.itcast.jms.consumer.IMessageSender;
import cn.itcast.jms.consumer.MessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

public class MessageReceiver implements MessageListener {
    @Autowired
    MessageFactory messageFactory;
    @Override
    public void onMessage(Message msg) {
        //1.将Message强制转换成MapMessage
        if(msg instanceof MapMessage){
            MapMessage message = (MapMessage)msg;
            try {
                //2.调用本类的方法处理具体要执行的消息
                processMsg(message);
                //3.设置手动相应

                message.acknowledge();
            }catch (JMSException e){
                e.printStackTrace();

            }
        }
    }

    private void processMsg(MapMessage message) {
        try {
            String type = message.getString("type");
            IMessageSender messageSender = messageFactory.createMessageSender(type);
            messageSender.sendMessage(message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
