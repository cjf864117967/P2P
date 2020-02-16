package cn.itcast.service.bank.impl;

import cn.itcast.dao.BankCardDAO;
import cn.itcast.domain.bankCardInfo.BankCardInfo;
import cn.itcast.service.bank.BankCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankCardServiceimpl implements BankCardService {
    @Autowired
    BankCardDAO bankCardDAO;
    @Override
    public BankCardInfo findByUserId(Integer userId) {
        return bankCardDAO.findByUserId(userId);
    }

    @Override
    public void addBankCardInfo(BankCardInfo bci) {
        bankCardDAO.save(bci);
    }
}