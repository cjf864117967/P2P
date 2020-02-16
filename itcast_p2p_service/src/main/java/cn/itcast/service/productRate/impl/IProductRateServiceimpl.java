package cn.itcast.service.productRate.impl;

import cn.itcast.dao.IProductRateDAO;
import cn.itcast.domain.product.ProductEarningRate;
import cn.itcast.service.productRate.IProductRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IProductRateServiceimpl implements IProductRateService {
    @Autowired
    IProductRateDAO iProductRateDAO;
    @Override
    public List<ProductEarningRate> findByProId(String pid) {
        return iProductRateDAO.findByProductId(Integer.parseInt(pid));
    }
}
