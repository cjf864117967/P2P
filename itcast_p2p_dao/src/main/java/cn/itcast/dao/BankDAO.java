package cn.itcast.dao;

import cn.itcast.domain.bankCardInfo.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankDAO extends JpaRepository<Bank,Integer> {
}
