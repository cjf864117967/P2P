package cn.itcast.service.product.impl;

import cn.itcast.dao.IProductRateDAO;
import cn.itcast.dao.ProductDAO;
import cn.itcast.domain.product.Product;
import cn.itcast.domain.product.ProductEarningRate;
import cn.itcast.service.product.IProductService;
import cn.itcast.utils.ProductStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.ws.Action;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceimpl implements IProductService {
    @Autowired
    ProductDAO productDAO;
    @Autowired
    IProductRateDAO iProductRateDAO;

    @Override
    public List<Product> findAll() {
        return productDAO.findAll();
    }

    @Override
    public Product findById(Long proId) {
        return productDAO.findOne(proId);
    }

    @Override
    public List<ProductEarningRate> findRateByProId(String proId) {
        return iProductRateDAO.findByProductId(Integer.parseInt(proId));
    }

    @Override
    public void update(Product product) {
        List<ProductEarningRate> pers = iProductRateDAO.findByProductId((int) product.getProId());
        if(pers !=null && pers.size()>0){
            iProductRateDAO.delByProId((int)product.getProId());
        }
        iProductRateDAO.save(product.getProEarningRate());
        productDAO.save(product);
    }






    @Override
    @Transactional(readOnly = true)
    public Product getById(Long valueOf) {
        Product product = productDAO.findOne(valueOf);
        List<ProductEarningRate> list = iProductRateDAO.getProductEarningRateByProductId(valueOf.intValue());
        product.setProEarningRate(list);
        changeStatusToChinese(product);
        return product;
    }
    private void changeStatusToChinese(Product product) {
        if (null == product){
            return;
        }
        List<Product> products = new ArrayList<>();
        products.add(product);
        changeStatusToChinese(products);
    }
    /**
     * 方法描述：将状态转换为中文
     *
     * @param products
     *            void
     */
    private void changeStatusToChinese(List<Product> products) {
        if (null == products)
            return;
        for (Product product : products) {
            int way = product.getWayToReturnMoney();
            // 每月部分回款
            if (ProductStyle.REPAYMENT_WAY_MONTH_PART.equals(String.valueOf(way))) {
                product.setWayToReturnMoneyDesc("每月部分回款");
                // 到期一次性回款
            } else if (ProductStyle.REPAYMENT_WAY_ONECE_DUE_DATE.equals(String.valueOf(way))) {
                product.setWayToReturnMoneyDesc("到期一次性回款");
            }

            // 是否复投 isReaptInvest 136：是、137：否
            // 可以复投
            if (ProductStyle.CAN_REPEAR == product.getIsRepeatInvest()) {
                product.setIsRepeatInvestDesc("是");
                // 不可复投
            } else if (ProductStyle.CAN_NOT_REPEAR == product.getIsRepeatInvest()) {
                product.setIsRepeatInvestDesc("否");
            }
            // 年利率
            if (ProductStyle.ANNUAL_RATE == product.getEarningType()) {
                product.setEarningTypeDesc("年利率");
                // 月利率 135
            } else if (ProductStyle.MONTHLY_RATE == product.getEarningType()) {
                product.setEarningTypeDesc("月利率");
            }

            if (ProductStyle.NORMAL == product.getStatus()) {
                product.setStatusDesc("正常");
            } else if (ProductStyle.STOP_USE == product.getStatus()) {
                product.setStatusDesc("停用");
            }

            // 是否可转让
            if (ProductStyle.CAN_NOT_TRNASATION == product.getIsAllowTransfer()) {
                product.setIsAllowTransferDesc("否");
            } else if (ProductStyle.CAN_TRNASATION == product.getIsAllowTransfer()) {
                product.setIsAllowTransferDesc("是");
            }
        }
    }
}
