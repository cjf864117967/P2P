package cn.itcast.service.product;

import cn.itcast.domain.product.Product;
import cn.itcast.domain.product.ProductEarningRate;

import java.util.List;

public interface IProductService {
    List<Product> findAll();

    Product findById(Long proId);

    List<ProductEarningRate> findRateByProId(String proId);

    void update(Product product);

    Product getById(Long valueOf);
}
