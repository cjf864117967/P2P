package cn.itcast.service.match.impl;

import cn.itcast.dao.ICreditorDAO;
import cn.itcast.dao.IFundingNotMatchedDAO;
import cn.itcast.dao.IMatchDAO;
import cn.itcast.domain.creditor.CreditorModel;
import cn.itcast.domain.productAccount.FundingNotMatchedModel;
import cn.itcast.service.match.IMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
public class IMatchServiceimpl implements IMatchService {
    @Autowired
    ICreditorDAO iCreditorDAO;


    @Autowired
    private EntityManagerFactory emf;
    @Autowired
    IFundingNotMatchedDAO iFundingNotMatchedDAO;

    @Override
    public void startMatch() {
        // 1. 得到所有待匹配的资金List<FundingNotMatchedModel> 10901未匹配
        List<FundingNotMatchedModel> fnmms = iFundingNotMatchedDAO.findMatchFundingNotMatched();
        // 2. 得到所有待匹配的债权List<CreditorModel> 已审核11302 未匹配 11403 部分匹配 11401
        List<CreditorModel> cms = iCreditorDAO.findMatchCreditor();

        match(fnmms, cms);

    }

    // 匹配操作
    private void match(List<FundingNotMatchedModel> fnmms, List<CreditorModel> cms) {
        // 3. 得到所有待匹配的资金金额
        BigDecimal investMoney = getInvestMoney(fnmms);

        // 4. 得到所有待匹配的债权的金额
        BigDecimal creditorMoney = getCreditorMoney(cms);

        // 5. 做两个队列，一个债权队列，一个是资金队列 LinkedList
        // 我们操作队列时，添加使用offer，从队列中获取使用poll
        // 这两个队列中存储的是没有匹配的信息
        LinkedList<FundingNotMatchedModel> investList = new LinkedList<FundingNotMatchedModel>();
        LinkedList<CreditorModel> creditorList = new LinkedList<CreditorModel>();

        // 6. 匹配操作分成三种情况
        // a) 资金>债权
        // b) 债权>资金
        // c) 资金==债权 （不用考虑，直接全部匹配成功）
        // 7. 债权>资金
        BigDecimal sub = creditorMoney.subtract(investMoney);
        if (sub.doubleValue() > 0) {
            // 债权大于资金
            // a. 需要使用前期定义的债权队列来装入未匹配的债权信息
            investList.addAll(fnmms);
            // b. 需要使用前期定义的资金队列来装入未匹配的资金信息
            creditorList.addAll(cms);
            // c. 使用while循环来判断资金队列不为空，一直匹配
            while (!investList.isEmpty()) { // 只要资金队列不为空，就循环

                // d. 从债权队列中获取债权信息与资金队列中的资金信息匹配
                CreditorModel cm = creditorList.pollFirst();
                FundingNotMatchedModel fnmm = investList.pollFirst();
                // 使用债权减去资金
                double cmMoney = cm.getAvailableMoney();
                double fnmmMoney = fnmm.getfNotMatchedMoney();
                BigDecimal bd = new BigDecimal(cmMoney).subtract(new BigDecimal(fnmmMoney));

                if (bd.doubleValue() > 0) {
                    cm.setAvailableMoney(bd.abs().doubleValue());
                    creditorList.offerFirst(cm);
                } else if (bd.doubleValue() < 0) {

                    fnmm.setfNotMatchedMoney(bd.abs().doubleValue());
                    investList.offerFirst(fnmm);

                }

            }
            // 循环结束，资金全部匹配了，而债权没有匹配
            // 1.修改债权状态
            udateCreditor(cms, creditorList);
            // 2.修改资金状态
            updateInvestStatus(fnmms);

        } else if (sub.doubleValue() < 0) {
            // 资金大于债权

            // 修改资金相关状态
            // updateInvestStatus(fnmms,investList)
            // 修改所有债权状态
            // updateCreditor(cms)
        } else {
            // 相等
            updateInvestStatus(fnmms);
            updateCreditorStatus(cms);
        }

    }

    // 判断债权匹配，修改债权状态
    // cms 原生的债权信息
    // clist 队列中的信息
    private void udateCreditor(List<CreditorModel> cms, LinkedList<CreditorModel> clist) {

        cms.removeAll(clist);
        // cms中乘下的是什么?这些就是匹配完的，调用一个方法修改所有的债权的状态
        updateCreditorStatus(cms);

        // clist中内容有可能是未匹配，或部分匹配
        for (CreditorModel cm : clist) {
            // 1.根据cm的id从数据库中查询
            Integer id = cm.getId();
            // CreditorModel _cm = creditorDao.findOne(id); //
            // 从数据库中根据id查询的,使用findOne是从一级缓存中查询
            CreditorModel _cm = emf.createEntityManager().find(CreditorModel.class, id); // 相当于是重新得到一个session
            if (_cm.getAvailableMoney() != cm.getAvailableMoney()) {
                // 部分匹配
                // cm.setMatchedStatus(11401);// 部分匹配
                iCreditorDAO.updateCreditorStatus(id, cm.getAvailableMoney(),
                        _cm.getAvailableMoney() - cm.getAvailableMoney(), 11401);
            }
        }

    }

    // 修改所有的债权状态
    private void updateCreditorStatus(List<CreditorModel> cms) {
        for (CreditorModel cm : cms) {
            iCreditorDAO.updateCreditorStatus(cm.getId(), 0, cm.getAvailableMoney(), 11402);
        }
    }

    // 修改所有资金状态
    private void updateInvestStatus(List<FundingNotMatchedModel> fnmms) {
        for (FundingNotMatchedModel fnmm : fnmms) {
            // fnmm.setfIsLocked(10905);
            // fnmm.setfNotMatchedMoney(0.0);
            iFundingNotMatchedDAO.updateStatus(fnmm.getfId());
        }
    }

    // 获取所有的债权可用金额
    private BigDecimal getCreditorMoney(List<CreditorModel> cms) {
        BigDecimal all = new BigDecimal(0);
        for (CreditorModel cm : cms) {
            all = all.add(new BigDecimal(cm.getAvailableMoney()));
        }
        return all;
    }

    // 获取所有的资金可用金额
    private BigDecimal getInvestMoney(List<FundingNotMatchedModel> fnmms) {
        BigDecimal all = new BigDecimal(0);
        for (FundingNotMatchedModel fnmm : fnmms) {
            all = all.add(new BigDecimal(fnmm.getfNotMatchedMoney()));
        }

        return all;
    }

    }