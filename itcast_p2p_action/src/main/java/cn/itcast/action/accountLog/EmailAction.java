package cn.itcast.action.accountLog;

import cn.itcast.action.common.BaseAction;
import cn.itcast.cache.BaseCacheService;
import cn.itcast.service.email.EmailService;
import cn.itcast.service.msm.IMSMService;
import cn.itcast.service.user.UserService;
import cn.itcast.utils.EmailUtils;
import cn.itcast.utils.Response;
import cn.itcast.utils.SecretUtil;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Namespace("/emailAuth")
@Controller
@Scope("prototype")
public class EmailAction extends BaseAction {

    @Autowired
    UserService userService;
    @Autowired
    BaseCacheService baseCacheService;
    @Autowired
    EmailService emailService;
    @Autowired
    IMSMService imsmService;
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
}
