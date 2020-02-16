package cn.itcast.dao;

import cn.itcast.domain.product.ProductAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductAccountDAO extends JpaRepository<ProductAccount,Integer>, JpaSpecificationExecutor<ProductAccount> {
}
