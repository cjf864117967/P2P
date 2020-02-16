package cn.itcast.dao;

import cn.itcast.domain.bankCardInfo.BankCardInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankCardDAO extends JpaRepository<BankCardInfo,Integer> {
    BankCardInfo findByUserId(Integer userId);
}
