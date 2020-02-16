package cn.itcast.service.bank;

import cn.itcast.domain.bankCardInfo.BankCardInfo;

public interface BankCardService {
    BankCardInfo findByUserId(Integer userId);

    void addBankCardInfo(BankCardInfo bci);
}
