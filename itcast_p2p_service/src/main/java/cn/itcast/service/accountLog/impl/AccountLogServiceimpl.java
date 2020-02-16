package cn.itcast.service.accountLog.impl;

import cn.itcast.dao.AccountLogDAO;
import cn.itcast.domain.productAccount.WaitMatchMoneyModel;
import cn.itcast.service.accountLog.AccountLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AccountLogServiceimpl implements AccountLogService {
    @Autowired
    AccountLogDAO accountLogDAO;


    //查询所有待匹配资金队列
    @Override
    public List<WaitMatchMoneyModel> selectWaitMatch() {
        List<Object[]> selectWaitMatch = accountLogDAO.findWaitMoney();
        List<WaitMatchMoneyModel> selectWaitMatchNew = new ArrayList<WaitMatchMoneyModel>();
        if (selectWaitMatch.size() > 0) {
            for (int i = 0; i < selectWaitMatch.size(); i++) {

                Object[] result = (Object[]) selectWaitMatch.get(i);
                WaitMatchMoneyModel w = new WaitMatchMoneyModel();
                w.setInvestRecordId((Integer) result[0]); // 投资记录id
                w.setUserId(((Long) result[1]).intValue()); // 用户id
                w.setStatus((Integer) result[2]); // 匹配状态
                w.setCurrentMonth((Integer) result[3]);//
                w.setDeadline((Integer) result[4]); // 周期
                w.setpSerialNo((String) result[5]);//
                w.setAmountWait((Double) result[6]);
                w.setInvestType((Integer) result[7]);
                w.setId((Integer) result[8]);
                w.setRecordedId((Integer) result[9]);
                w.setConfirmDate((Timestamp) result[10]);// 确认时间
                w.setFundWeight((Integer) result[11]);// 权重
                w.setUserName((String) result[12]); // 用户名
                w.setProductName((String) result[13]);

                if (w.getInvestType() == 124) {
                    w.setInvestTypeDescrible("新增投资");
                }
                if (w.getInvestType() == 125) {
                    w.setInvestTypeDescrible("回款再投资");
                }
                if (w.getInvestType() == 126) {
                    w.setInvestTypeDescrible("到期结清");
                }
                if (w.getInvestType() == 127) {
                    w.setInvestTypeDescrible("提前结清");
                }

                DecimalFormat df = new DecimalFormat("0.00");

                String valueOf = df.format(w.getAmountWait());

                w.setAmountWaitDes(valueOf);
                selectWaitMatchNew.add(w);
            }
        } else {
            selectWaitMatchNew.add(null);
        }
        return selectWaitMatchNew;

    }

    //查询所有待匹配资金队列统计信息
    @Override
    public WaitMatchMoneyModel selectWaitMatchCount() {
        List<Object[]> objes = accountLogDAO.findWaitMoneySum();

        WaitMatchMoneyModel wmm = new WaitMatchMoneyModel();
        wmm.setCount(((Long) (objes.get(0)[0])).intValue());
        wmm.setSum((Double) objes.get(0)[1]);
        return wmm;

    }
}
