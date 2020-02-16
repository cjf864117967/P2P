package cn.itcast.action.verification;

import cn.itcast.action.common.BaseAction;
import cn.itcast.cache.BaseCacheService;
import cn.itcast.domain.user.UserModel;
import cn.itcast.service.email.EmailService;
import cn.itcast.service.msm.IMSMService;
import cn.itcast.service.user.UserService;
import cn.itcast.utils.*;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Map;

@Namespace("/verification")
@Controller
@Scope("prototype")
public class VerificationAction extends BaseAction{
    @Autowired
    UserService userService;
    @Autowired
    BaseCacheService baseCacheService;
    @Autowired
    EmailService emailService;
    @Autowired
    IMSMService imsmService;
    @Action("sendMessage")
    public void sendMessage(){
        String token = this.getRequest().getHeader("token");
//        String token = GetHttpResponseHeader.getHeadersInfo(this.getRequest());
        Map<String, Object> hmap = baseCacheService.getHmap(token);
        //1.得到电话号
        String phone = this.getRequest().getParameter("phone");
        //2.调用发送短信的工具
        String randomNumeric = RandomStringUtils.randomNumeric(6);
        String content ="P2P短信认证"+randomNumeric;
        imsmService.sendMsm(phone,content);
        System.out.println("向"+phone+"发送验证消息:"+content);
//        try {
//            MSMUtils.SendSms(phone,content);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        baseCacheService.set(phone,randomNumeric);
        baseCacheService.expire(phone,3*60);
        try {
            this.getResponse().getWriter().write(Response.build().setStatus("1").toJSON());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Action("addPhone")
    public void addPhone(){
        String token = this.getRequest().getHeader("token");
        try {
            if (StringUtils.isEmpty(token)) {
                // token过期
                this.getResponse().getWriter()
                        .write(Response.build().setStatus(FrontStatusConstants.NULL_TOKEN).toJSON());
                return;
            }
            Map<String, Object> hmap = baseCacheService.getHmap(token);
            if (hmap == null || hmap.size() == 0) {
                // 用户未登录
                this.getResponse().getWriter()
                        .write(Response.build().setStatus(FrontStatusConstants.NOT_LOGGED_IN).toJSON());
                return;

            }
            // 1.获取请求参数
            String phone = this.getRequest().getParameter("phone");
            String phoneCode = this.getRequest().getParameter("phoneCode");
            // 2.判断验证码是否正确
            String _phoneCode = baseCacheService.get(phone);

            if (!phoneCode.equals(_phoneCode)) {
                // 说明不正确
                this.getResponse().getWriter()
                        .write(Response.build().setStatus(FrontStatusConstants.INPUT_ERROR_OF_VALIDATE_CARD).toJSON());
                return;
            }
            // 3.得到用户，判断用户是否已经绑定手机
            UserModel user = userService.findById((int) (hmap.get("id")));
            if (user.getPhoneStatus() == 1) {
                this.getResponse().getWriter()
                        .write(Response.build().setStatus(FrontStatusConstants.MOBILE_ALREADY_REGISTER).toJSON());
                return;
            }

            // 4.绑定手机
            userService.updatePhoneStatus(phone, (int) (hmap.get("id")));
            this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).toJSON());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Action("verifiRealName")
    public void verifiRealName(){
//        String token = GetHttpResponseHeader.getHeadersInfo(this.getRequest());
        String token = this.getRequest().getHeader("token");
        try {
            if (StringUtils.isEmpty(token)) {
                // token过期
                this.getResponse().getWriter()
                        .write(Response.build().setStatus(FrontStatusConstants.NULL_TOKEN).toJSON());
                return;
            }
            Map<String, Object> hmap = baseCacheService.getHmap(token);
            if (hmap == null || hmap.size() == 0) {
                // 用户未登录
                this.getResponse().getWriter()
                        .write(Response.build().setStatus(FrontStatusConstants.NOT_LOGGED_IN).toJSON());
                return;

            }
            //获取
            String realName = this.getRequest().getParameter("realName");
            String identity = this.getRequest().getParameter("identity");
            //修改实名状态，身份证号实名
            userService.updateRealName(realName,identity,(int)hmap.get("id"));
            this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).toJSON());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Action("auth")
    public void auth(){
        //1.得到请求参数
        String userId = this.getRequest().getParameter("userId");
        String username = this.getRequest().getParameter("username");
        String email = this.getRequest().getParameter("email");
        //2.调用service完成发送邮件操作
        String title = "P2P邮箱认证激活";
        String enc = null;
        try {
            enc = SecretUtil.encrypt(userId);
            String content = EmailUtils.getMailCapacity(email, enc, username);
            emailService.sendEmail(email,title,content);
            userService.addEmail(email,Integer.parseInt(userId));
            this.getResponse().getWriter().write(Response.build().setStatus("1").toJSON());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Action("emailactivation")
    public void emailactivation(){
        this.getResponse().setContentType("text/html;charset=utf-8");
        //1.得到us
        String us = this.getRequest().getParameter("us");
        try {
            String userId = SecretUtil.decode(us);
            userService.updateEmailStatus(Integer.parseInt(userId));
            this.getResponse().getWriter().write("邮箱认证成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Action("validateSMS")
    public void validateSMS(){
        try {
            this.getResponse().getWriter().write(Response.build().setStatus("1").toJSON());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
