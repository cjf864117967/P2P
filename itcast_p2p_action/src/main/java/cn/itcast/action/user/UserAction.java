package cn.itcast.action.user;

import cn.itcast.action.common.BaseAction;
import cn.itcast.action.filter.GetHttpResponseHeader;
import cn.itcast.cache.BaseCacheService;
import cn.itcast.domain.user.UserAccountModel;
import cn.itcast.domain.user.UserModel;
import cn.itcast.service.user.UserAccountModelService;
import cn.itcast.service.user.UserService;
import cn.itcast.utils.*;
import com.opensymphony.xwork2.ModelDriven;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Controller
@Namespace("/user")
@Scope("prototype")
public class UserAction extends BaseAction implements ModelDriven<UserModel> {
    @Autowired
    UserService userService;
    @Autowired
    UserAccountModelService userAccountModelService;
    private UserModel user = new UserModel();
    @Override
    public UserModel getModel() {
        return user;
    }
    @Autowired
    BaseCacheService baseCacheService;
//    @Resource(name = "redisCache")
//    private BaseCacheService baseCacheService;

    @Action("uuid")
    public void uuid(){
        String uuid = UUID.randomUUID().toString();
        //存储到redis
        baseCacheService.set(uuid,uuid);
        baseCacheService.expire(uuid,3*60);
        //响应到浏览器
        try {
            this.getResponse().getWriter().write(Response.build().setStatus("1").setUuid(uuid).toJSON());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Action("validateCode")
    public void validateCode(){
        String tokenUuid = this.getRequest().getParameter("tokenUuid");
        String uuid = baseCacheService.get(tokenUuid);
            try {
                if(StringUtils.isEmpty(uuid)){
                    this.getResponse().getWriter().write(Response.build().setStatus("-999").toJSON());
                    return;
                }
                String str = ImageUtil.getRundomStr();
                //将信息存储到redis
                baseCacheService.del(uuid);
                baseCacheService.set(tokenUuid,str);
                baseCacheService.expire(tokenUuid,3*60);
                //将验证码图片加载到浏览器
                ImageUtil.getImage(str,this.getResponse().getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    @Action("validateUserName")
    public void validateUserName(){
        String username = this.getRequest().getParameter("username");
        UserModel user = userService.findByUserName(username);

            try {
                if(user == null){
                    this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).toJSON());
                    return;

                }else{
                    this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.ALREADY_EXIST_OF_USERNAME).toJSON());
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
    @Action("validatePhone")
    public void validatePhone(){
        String phone = this.getRequest().getParameter("phone");
        UserModel user = userService.findByPhone(phone);

        try {
            if(user == null){
                this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).toJSON());
                return;

            }else{
                this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.MOBILE_ALREADY_REGISTER).toJSON());
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Action("codeValidate")
    public void codeValidate(){
        String signUuid = this.getRequest().getParameter("signUuid");
        String signCode = this.getRequest().getParameter("signCode");
        String _signCode = baseCacheService.get(signUuid);

            try {
                if(StringUtils.isEmpty(signCode)){
                    this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NULL_OF_VALIDATE_CARD).toJSON());
                    return;
                }
                if(StringUtils.isEmpty(_signCode)){

                    this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NULL_OF_VALIDATE_CARD).toJSON());
                    return;
                }
                if(signCode.equalsIgnoreCase(_signCode)){
                    this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).toJSON());
                    return;
                }else{
                    this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.INPUT_ERROR_OF_VALIDATE_CARD).toJSON());
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    @Action("signup")
    public void regist(){



        String pwdMd5 = MD5Util.md5(user.getUsername()+user.getPassword().toLowerCase());

        user.setPassword(pwdMd5);

        boolean flag = userService.addUser(user);
            try {
                if(flag){
                    //注册成功
                    //开账户
                    userAccountModelService.add(user.getId());
                    //将用户存储到redis中，有效时间为30分钟
                    String token = generateUserToken(user.getUsername());
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("id",user.getId());
                    this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).setData(map).setToken(token).toJSON());
                }else{
                    this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.BREAK_DOWN).toJSON());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
    public String generateUserToken(String userName) {

        try {
            // 生成令牌
            String token = TokenUtil.generateUserToken(userName);

            // 根据用户名获取用户
            UserModel user = userService.findByUserName(userName);
            // 将用户信息存储到map中。
            Map<String, Object> tokenMap = new HashMap<String, Object>();
            tokenMap.put("id", user.getId());
            tokenMap.put("userName", user.getUsername());
            tokenMap.put("phone", user.getPhone());
            tokenMap.put("userType", user.getUserType());
            tokenMap.put("payPwdStatus", user.getPayPwdStatus());
            tokenMap.put("emailStatus", user.getEmailStatus());
            tokenMap.put("realName", user.getRealName());
            tokenMap.put("identity", user.getIdentity());
            tokenMap.put("realNameStatus", user.getRealNameStatus());
            tokenMap.put("payPhoneStatus", user.getPhoneStatus());

            baseCacheService.del(token);
            baseCacheService.setHmap(token, tokenMap); // 将信息存储到redis中

            // 获取配置文件中用户的生命周期，如果没有，默认是30分钟
            String tokenValid = ConfigurableConstants.getProperty("30", "30");
            tokenValid = tokenValid.trim();
            baseCacheService.expire(token, Long.valueOf(tokenValid) * 60);

            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return Response.build().setStatus("-9999").toJSON();
        }
    }

    @Action("login")
    public void login(){

        //得到请求参数
        String signUuid = this.getRequest().getParameter("signUuid");
        String signCode = this.getRequest().getParameter("signCode");
        //验证
        //非空判断
        //判断验证码
        try {

            String _signCode = baseCacheService.get(signUuid);
            if(!_signCode.equalsIgnoreCase(signCode)){
                this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.INPUT_ERROR_OF_VALIDATE_CARD).toJSON());
                return;
            }
            //调用service完成登录操作
            //判断是否是手机号
            String str = user.getUsername();
            if(CommomUtil.isMobile(str)){
                UserModel um = userService.findByPhone(str);
                str = um.getUsername();
                //调用service的根据username，password查询用户操作
            }
//            String password = user.getPassword();
            String pwd = MD5Util.md5(str+user.getPassword().toLowerCase());
            UserModel userModel = userService.login(str,pwd);
            if(userModel!=null){
                String token = generateUserToken(userModel.getUsername());
                HashMap<String, Object> data = new HashMap<>();
                data.put("userName", userModel.getUsername());
                data.put("id",userModel.getId());
                //相应数据到浏览器
                this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).setData(data).setToken(token).toJSON());
            }else{
                this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.ERROR_OF_USERNAME_PASSWORD).toJSON());
            }
        }catch (Exception e){
            e.printStackTrace();



        }
    }



    @Action("logout")
    public void logout(){
        String token = this.getRequest().getHeader("token");
//        String string = baseCacheService.get(token);

            try {
                if(StringUtils.isNotEmpty(token)){
//                    this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NULL_TOKEN).toJSON());
//                    return;
                    baseCacheService.del(token);
                }
//                baseCacheService.del(token);
                this.getResponse().getWriter().write(Response.build().setStatus("1").toJSON());
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
    @Action("userSecure")
    public void userSecure(){
        //1.得到token
//        String token = GetHttpResponseHeader.getHeadersInfo(this.getRequest());
        String token = this.getRequest().getHeader("token");
        //2.从redis中根据token获取信息，可以获取到用户id
        Map<String, Object> hmap = baseCacheService.getHmap(token);
        int userId = (int) hmap.get("id");
        //3.根据用户id，调用service，获取用户对象
        UserModel u = userService.findById(userId);

        //4.封装相应浏览器的数据，响应到浏览器
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> data = new HashMap<>();
        data.put("phoneStatus",u.getPhoneStatus());
        data.put("realNameStatus",u.getRealNameStatus());
        data.put("payPwdStatus",u.getPayPwdStatus());
        data.put("emailStatus",u.getEmailStatus());
        list.add(data);
        try {
            this.getResponse().getWriter().write(Response.build().setStatus("1").setData(list).toJSON());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Action("accountHomepage")
    public void accountHomepage(){
        String token = this.getRequest().getHeader("token");
        Map<String, Object> hmap = baseCacheService.getHmap(token);
        int userId = (int) hmap.get("id");
        UserAccountModel uam = userAccountModelService.findByUserId(userId);
        ArrayList<JSONObject> list = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("u_total",uam.getTotal());
        jsonObject.put("u_balance",uam.getBalance());
        jsonObject.put("u_interest_a",uam.getInterestA());
        list.add(jsonObject);
        try {
            this.getResponse().getWriter().write(Response.build().setStatus("1").setData(list).toJSON());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Action("userSecureDetailed")
    public void userSecureDetailed(){
        //1.得到token
//        String token = GetHttpResponseHeader.getHeadersInfo(this.getRequest());
        String token = this.getRequest().getHeader("token");
        //2.从redis中根据token获取信息，可以获取到用户id
        Map<String, Object> hmap = baseCacheService.getHmap(token);
        int userId = (int) hmap.get("id");
        //3.根据用户id，调用service，获取用户对象
        UserModel u = userService.findById(userId);

        //4.封装相应浏览器的数据，响应到浏览器
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> data = new HashMap<>();
        data.put("phoneStatus",u.getPhoneStatus());
        data.put("realNameStatus",u.getRealNameStatus());
        data.put("payPwdStatus",u.getPayPwdStatus());
        data.put("emailStatus",u.getEmailStatus());
        data.put("passwordstatus",u.getPassword());
        data.put("username",u.getUsername());
        data.put("phone",u.getPhone());
        list.add(data);
        try {
            this.getResponse().getWriter().write(Response.build().setStatus("1").setData(list).toJSON());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Action("addPhone")
    public void addPhone(){
        String token = this.getRequest().getHeader("token");
        try {
            if (org.apache.commons.lang3.StringUtils.isEmpty(token)) {
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
}
