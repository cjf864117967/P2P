package cn.itcast.action.productRate;

import cn.itcast.action.common.BaseAction;
import cn.itcast.domain.product.ProductEarningRate;
import cn.itcast.service.productRate.IProductRateService;
import cn.itcast.utils.Response;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Namespace("/productRate")
@Controller
@Scope("prototype")
public class ProductRateAction extends BaseAction {
    @Autowired
    IProductRateService iProductRateService;
    @Action("yearInterest")
    public void yearInterest(){



        //得到理财产品的id
        String pid = this.getRequest().getParameter("pid");
        //根据id查询理财产品的利率
        List<ProductEarningRate> pers =  iProductRateService.findByProId(pid);
        //将数据处理
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (ProductEarningRate per : pers) {
            Map<String, Object> map = new HashMap<>();
            map.put("incomeRate",per.getIncomeRate());
            map.put("endMonth",per.getMonth());
            list.add(map);
        }
        try {
            this.getResponse().getWriter().write(Response.build().setStatus("1").setData(list).toJSON());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
