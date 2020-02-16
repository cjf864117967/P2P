package cn.itcast.utils.creditor.impl;

import cn.itcast.domain.creditor.CreditorModel;
import cn.itcast.utils.creditor.CreditorDao4Sql;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class CreditorDao4Sqlimpl implements CreditorDao4Sql {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<CreditorModel> getCreditorList(String dDebtNo, String dContractNo, String dDebtTransferredDateStart, String dDebtTransferredDateEnd, Integer dDebtStatus, Integer dMatchedStatus, Integer perPage, Integer start) {
        String sql = "select * from t_debt_info  a,(select d_id,rownum as rn from t_debt_info)  b where 1=1";
        if (StringUtils.isNotEmpty(dDebtNo)) {
            sql += " and d_debt_no='" + dDebtNo + "'";
        }
        if (StringUtils.isNotEmpty(dContractNo)) {
            sql += " and d_contract_No='" + dContractNo + "'";
        }
        if (dDebtStatus != null && dDebtStatus != 0) {
            sql += " and d_debt_Status=" + dDebtStatus;
        }
        if (dMatchedStatus != null && dMatchedStatus != 0) {
            sql += " and d_matched_Status=" + dMatchedStatus;
        }
        if (dDebtTransferredDateStart != null && dDebtTransferredDateEnd != null) {
            sql += " and d_debt_Transferred_Date between  to_date('" + dDebtTransferredDateStart + "','yyyy-mm-dd') and to_date('"
                    + dDebtTransferredDateEnd + "','yyyy-mm-dd')";
        }
        if (perPage != null && perPage != 0 && start != null) {
            sql += " and (b.rn >" + start + " and b.rn<=" + (start + perPage)+")";
        }
        sql += " and a.d_id=b.d_id order by d_debt_no desc";
        System.out.println(sql);
        Query query = em.createNativeQuery(sql, CreditorModel.class);
        return query.getResultList();
//        return null;
    }

    @Override
    public List<Object[]> getCreditorSum(String dDebtNo, String dContractNo, String dDebtTransferredDateStart, String dDebtTransferredDateEnd, Integer dDebtStatus, Integer dMatchedStatus, Integer perPage, Integer start) {
        String sql = "select count(*) as dIdCount,sum(d_debt_Transferred_Money) as dDebtMoneySum, sum(d_available_Money) as dAvailableMoneySum  from t_debt_info where 1=1";
        if (StringUtils.isNotEmpty(dDebtNo)) {
            sql += " and d_debt_no='" + dDebtNo + "'";
        }
        if (StringUtils.isNotEmpty(dContractNo)) {
            sql += " and d_contract_No='" + dContractNo + "'";
        }
        if (dDebtStatus != null && dDebtStatus != 0) {
            sql += " and d_debt_Status=" + dDebtStatus;
        }
        if (dMatchedStatus != null && dMatchedStatus != 0) {
            sql += " and d_matched_Status=" + dMatchedStatus;
        }
        if (dDebtTransferredDateStart != null && dDebtTransferredDateEnd != null) {
            sql += " and d_debt_Transferred_Date between  to_date('" + dDebtTransferredDateStart + "','yyyy-mm-dd') and to_date('"
                    + dDebtTransferredDateEnd + "','yyyy-mm-dd')";
        }
        Query query = em.createNativeQuery(sql);
        List<Object[]> result = query.getResultList();
        return result;
//        return null;
    }
}
