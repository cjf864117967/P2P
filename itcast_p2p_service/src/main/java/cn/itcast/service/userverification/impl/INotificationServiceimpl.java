package cn.itcast.service.userverification.impl;

import cn.itcast.dao.IUserDao;
import cn.itcast.domain.user.UserModel;
import cn.itcast.service.message.EmailMessageService;
import cn.itcast.service.message.SMSMessageService;
import cn.itcast.service.user.IUserMessageService;
import cn.itcast.service.userverification.INotificationService;
import org.hibernate.validator.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class INotificationServiceimpl implements INotificationService {
    @Autowired
    IUserDao iUserDao;



    @Autowired
    IUserMessageService userMessageService;




    @Autowired
    EmailMessageService emailMessageService;
    @Autowired
    SMSMessageService smsMessageService;
    @Override
    public void sendMessage(String userId, String operationType, String messageblob) {
        String email = null;
        String phone = null;
        //1.通过用户id 查询 用户的手机号 ,邮箱
        UserModel user = iUserDao.findOne(Integer.parseInt(userId));
        if(null != user){
            email = user.getEmail();
            phone = user.getPhone();
        }
        userMessageService.insertMessages(userId, messageblob);
        emailMessageService.sendMail("邮件通知", email, messageblob);
        smsMessageService.sendSMS(phone, messageblob);
    }
}
