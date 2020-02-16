package cn.itcast.dao;

import cn.itcast.domain.product.ProductEarningRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IProductRateDAO extends JpaRepository<ProductEarningRate,Integer> {
    List<ProductEarningRate> findByProductId(int parseInt);
    @Modifying
    @Query("delete from ProductEarningRate per where per.productId=?1")
    void delByProId(int proId);

    ProductEarningRate getEarningRateByProductIdAndMonth(int pid, int mounth);

    List<ProductEarningRate> getProductEarningRateByProductId(int intValue);
}
