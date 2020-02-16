package cn.itcast.action.product;

import cn.itcast.action.common.BaseAction;
import cn.itcast.domain.product.Product;
import cn.itcast.domain.product.ProductEarningRate;
import cn.itcast.service.product.IProductService;
import cn.itcast.utils.FrontStatusConstants;
import cn.itcast.utils.JsonMapper;
import cn.itcast.utils.Response;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@Scope("prototype")
@Namespace("/product")
public class ProductAction extends BaseAction implements ModelDriven<Product> {
    private Product product = new Product();
    @Override
    public Product getModel() {
        return product;
    }
    @Autowired
    IProductService productService;
    @Action("findAllProduct")
    public void findAll(){
        List<Product> ps = productService.findAll();
        try {
            this.getResponse().setCharacterEncoding("utf-8");
            this.getResponse().getWriter().write(Response.build().setStatus("1").setData(ps).toJSON());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Action("findProductById")
    public void findById(){
        this.getResponse().setCharacterEncoding("utf-8");
        String proId = this.getRequest().getParameter("proId");
        Product p = productService.findById(Long.parseLong(proId));
        try {
            this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).setData(p).toJSON());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Action("findRates")
    public void findRates(){
        String proId = this.getRequest().getParameter("proId");
        List<ProductEarningRate> pers = productService.findRateByProId(proId);
        try {
            this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).setData(pers).toJSON());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Action("modifyProduct")
    public void modifyProduct(){
        String proEarningRates = this.getRequest().getParameter("proEarningRates");
        Map map = new JsonMapper().fromJson(proEarningRates, Map.class);
        List<ProductEarningRate> pers = new ArrayList<>();
        for (Object key : map.keySet()){
            ProductEarningRate per = new ProductEarningRate();
            per.setMonth(Integer.parseInt(key.toString()));
            per.setIncomeRate(Double.parseDouble(map.get(key).toString()));
            per.setProductId((int)product.getProId());
            pers.add(per);
        }

        product.setProEarningRate(pers);
        productService.update(product);



        try {
            this.getResponse().getWriter().write(Response.build().setStatus(FrontStatusConstants.SUCCESS).toJSON());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
