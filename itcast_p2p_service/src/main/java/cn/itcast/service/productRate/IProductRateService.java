package cn.itcast.service.productRate;

import cn.itcast.domain.product.ProductEarningRate;

import java.util.List;

public interface IProductRateService {
    List<ProductEarningRate> findByProId(String pid);
}
