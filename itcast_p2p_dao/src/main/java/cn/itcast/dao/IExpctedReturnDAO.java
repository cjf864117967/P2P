package cn.itcast.dao;

import cn.itcast.domain.productAccount.ExpectedReturn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IExpctedReturnDAO extends JpaRepository<ExpectedReturn,Integer> {
}
