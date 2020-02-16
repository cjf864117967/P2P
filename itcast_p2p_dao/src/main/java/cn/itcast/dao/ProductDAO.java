package cn.itcast.dao;

import cn.itcast.domain.product.Product;
import cn.itcast.domain.product.ProductEarningRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductDAO extends JpaRepository<Product,Long> {

}
