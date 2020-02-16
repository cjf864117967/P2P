package cn.itcast.action.investment;

import cn.itcast.action.common.BaseAction;
import cn.itcast.action.filter.GetHttpResponseHeader;
import cn.itcast.cache.BaseCacheService;
import cn.itcast.domain.user.UserAccountModel;
import cn.itcast.service.user.UserAccountModelService;
import cn.itcast.utils.Response;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Map;

@Namespace("/investment")
@Controller
@Scope("prototype")
public class InvestmentAction extends BaseAction {
    @Autowired
    BaseCacheService baseCacheService;
    @Autowired
    UserAccountModelService userAccountModelService;
    @Action("checkAccount")
    public void checkAccount(){
        //1.得到请求参数，投资的金额
        double account = Double.parseDouble(this.getRequest().getParameter("account"));
        //2.得到账户的信息
        String token = this.getRequest().getHeader("token");
//        String token = GetHttpResponseHeader.getHeadersInfo(this.getRequest());
        Map<String, Object> hmap = baseCacheService.getHmap(token);
        try {
            if(hmap == null || hmap.size()==0){
                this.getResponse().getWriter().write(Response.build().setStatus("15").toJSON());
            }
            int userId = (int) hmap.get("id");
            UserAccountModel uam = userAccountModelService.findByUserId(userId);
            double balance = uam.getBalance();
            if(balance>=account){
                this.getResponse().getWriter().write(Response.build().setStatus("1").toJSON());
                return;
            }else{
                this.getResponse().getWriter().write(Response.build().setStatus("17").toJSON());
                return;




            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
