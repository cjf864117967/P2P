package cn.itcast.service.email.impl;

import cn.itcast.service.email.EmailService;
import cn.itcast.utils.IMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceimpl implements EmailService {
//    @Autowired
//    JavaMailSender mailSender;
      @Autowired
      JmsTemplate jmsTemplate;

    public void sendEmail(String email,String title,String content){
        //创建一个邮件信息
//        MimeMessage mm = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(mm);
//        try {
//            helper.setFrom("864117967@qq.com");
//            helper.setSubject(title);
//            helper.setText(content);
//            helper.setTo(email);
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//        mailSender.send(mm);
        Map<String, Object> map = new HashMap<>();
        map.put(IMessage.MessageType,IMessage.EmailMessage);
        map.put(IMessage.MessageContent,content);
        map.put(IMessage.EmailMessage,email);

        jmsTemplate.convertAndSend(map);
    }
}
