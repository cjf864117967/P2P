package cn.itcast.action.bankCardInfo;

import cn.itcast.action.common.BaseAction;
import cn.itcast.cache.BaseCacheService;
import cn.itcast.domain.bankCardInfo.Bank;
import cn.itcast.domain.bankCardInfo.BankCardInfo;
import cn.itcast.domain.city.City;
import cn.itcast.domain.user.UserModel;
import cn.itcast.service.bank.BankCardService;
import cn.itcast.service.bank.BankService;
import cn.itcast.service.city.CityService;
import cn.itcast.service.user.UserService;
import cn.itcast.utils.FrontStatusConstants;
import cn.itcast.utils.Response;
import cn.itcast.utils.TokenUtil;
import com.opensymphony.xwork2.ModelDriven;
import jdk.nashorn.internal.parser.Token;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Namespace("/bankCardInfo")
@Controller
@Scope("prototype")
public class BankCardInfoAction extends BaseAction implements ModelDriven<BankCardInfo> {
    private BankCardInfo bci = new BankCardInfo();
    @Override
    public BankCardInfo getModel() {
        return bci;
    }
    @Autowired
    BaseCacheService baseCacheService;
    @Autowired
    BankCardService bankCardService;
    @Autowired
    BankService bankService;
    @Autowired
    CityService cityService;
    @Autowired
    UserService userService;
    @Action("findBankInfoByUsername")
    public void findBankInfoByUsername(){
        String token = this.getRequest().getHeader("token");

        try {
            if(StringUtils.isEmpty(token)){
                this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NULL_TOKEN).toJSON());
            }

            Map<String, Object> hmap = baseCacheService.getHmap(token);
            if(hmap == null || hmap.size()==0){
                this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NOT_LOGGED_IN).toJSON());
            }
            String _username = TokenUtil.getTokenUserName(token);
            //得到请求参数
            String username = this.getRequest().getParameter("username");
            //验证用户名正确
            Integer userId = null;
            if(_username.startsWith(username)){
                if(userId == null){
                    userId= (Integer) hmap.get("id");

                    BankCardInfo bci  = bankCardService.findByUserId(userId);
                    if(bci !=null){
                        this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).setData(bci).toJSON());
                        return;
                    }else{
                        this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.BREAK_DOWN).toJSON());
                        return;
                    }
                }
            }else{
                this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NOT_LOGGED_IN).toJSON());
            }




        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Action("findAllBanks")
    public void findAllBanks(){
        this.getResponse().setCharacterEncoding("utf-8");
        String token = this.getRequest().getHeader("token");
        List<Bank> banks = bankService.findAll();
        try {
            this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).setData(banks).toJSON());
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
    @Action("findProvince")
    public void findProvince(){
        this.getResponse().setCharacterEncoding("utf-8");
        String token = this.getRequest().getHeader("token");
        List<City> cs = cityService.findProvince();
        try {
            this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).setData(cs).toJSON());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Action("findUserInfo")
    public void findUserInfo(){
        this.getResponse().setCharacterEncoding("utf-8");
        String token = this.getRequest().getHeader("token");
        try {
            if(StringUtils.isEmpty(token)){

                this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NULL_TOKEN).toJSON());
            }
            Map<String, Object> hmap = baseCacheService.getHmap(token);
            //没有登录
            if(hmap == null || hmap.size()==0){
                this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.NOT_LOGGED_IN).toJSON());
            }
            String _username = TokenUtil.getTokenUserName(token);
            String username = this.getRequest().getParameter("username");
            Integer userId = null;
            if(_username.startsWith(username)){
                userId = (Integer) hmap.get("id");
            }
            //根据userId查询用户信息
            UserModel userModel = userService.findById(userId);
            this.getResponse().getWriter().write(Response.build().setStatus("1").setData(userModel).toJSON());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Action("findCity")
    public void findCity(){
        this.getResponse().setCharacterEncoding("utf-8");
        String token = this.getRequest().getHeader("token");
        //1.得到请求参数
        String cityAreaNum = this.getRequest().getParameter("cityAreaNum");
        //2.调用CityService查询数据
        List<City> cs = cityService.findByParentCityAreaNum(cityAreaNum);
        try {
            this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).setData(cs).toJSON());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Action("addBankCardInfo")
    public void addBankCardInfo(){
        String token = this.getRequest().getHeader("token");
        //1.得到请求参数--使用模型驱动
        //2.查询用户是否绑定银行卡
        int userId = (int) baseCacheService.getHmap(token).get("id");
        BankCardInfo bankCardInfo = bankCardService.findByUserId(userId);

        try {
            if(bankCardInfo == null){
                //可以绑定
                bci.setUserId(userId);//手动绑定用户的id
                bankCardService.addBankCardInfo(bci);
                this.getResponse().getWriter().write(Response.build().setStatus("1").toJSON());
                return;
            }else{
                this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.CARDINFO_ALREADY_EXIST).toJSON());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
