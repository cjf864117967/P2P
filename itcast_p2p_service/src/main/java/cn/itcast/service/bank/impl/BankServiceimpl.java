package cn.itcast.service.bank.impl;

import cn.itcast.dao.BankDAO;
import cn.itcast.domain.bankCardInfo.Bank;
import cn.itcast.service.bank.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankServiceimpl implements BankService {
    @Autowired
    BankDAO bankDAO;

    @Override
    public List<Bank> findAll() {
        return bankDAO.findAll();
    }
}
