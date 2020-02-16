package cn.itcast.action.user;

import cn.itcast.action.common.BaseAction;
import cn.itcast.cache.BaseCacheService;
import cn.itcast.domain.user.UserAccountModel;
import cn.itcast.service.user.UserAccountModelService;
import cn.itcast.utils.Response;
import net.sf.json.JSONObject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@Controller
@Namespace("/account")
@Scope("prototype")
public class UserAccountModelAction extends BaseAction {
    @Autowired
    BaseCacheService baseCacheService;

    @Autowired
    UserAccountModelService userAccountModelService;
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
}
