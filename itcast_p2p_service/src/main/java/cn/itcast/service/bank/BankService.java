package cn.itcast.service.bank;

import cn.itcast.domain.bankCardInfo.Bank;

import java.util.List;

public interface BankService {
    List<Bank> findAll();
}
