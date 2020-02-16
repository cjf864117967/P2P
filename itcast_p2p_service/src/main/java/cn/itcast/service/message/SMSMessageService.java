package cn.itcast.service.message;

import cn.itcast.utils.IMessage;

import java.util.List;

public interface SMSMessageService extends IMessage {
    /**
     * 单条短信发送
     * @param to 接收者
     * @param content 内容
     */
    public void sendSMS(String to, String content);

    /**
     * 批量短信发送
     * @param to 接收者
     * @param content 内容
     */
    public void sendSMS(List<String> to, String content);
}
