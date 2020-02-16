package cn.itcast.service.accountLog;

import cn.itcast.domain.productAccount.WaitMatchMoneyModel;

import java.util.List;

public interface AccountLogService {

    List<WaitMatchMoneyModel> selectWaitMatch();

    WaitMatchMoneyModel selectWaitMatchCount();
}
