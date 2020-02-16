package cn.itcast.service.message;

import java.util.List;

public interface EmailMessageService {
    /**
     * 发送单个邮件
     * @param title 邮件标题
     * @param to 接收者
     * @param content 发送内容
     */
    public void sendMail(String title , String to , String content);

    /**
     * 发送单个邮件
     * @param title 邮件标题
     * @param to 接收者
     * @param content 发送内容
     */
    public void sendMail(String title , String to , String cc, String content);
    /**
     * 批量发送邮件
     * @param title 邮件标题
     * @param to 接收者
     * @param content 发送内容
     */
    public void sendMail(String title , List<String> to , String content);

    /**
     * 批量发送邮件
     * @param title 邮件标题
     * @param to 接收者
     * @param cc 抄送者
     * @param content 发送内容
     */
    public void sendMail(String title , List<String> to , List<String> cc , String content);
}
