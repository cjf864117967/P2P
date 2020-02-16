package cn.itcast.utils.creditor;

import cn.itcast.domain.creditor.CreditorModel;

import java.util.List;

public interface CreditorDao4Sql {
    List<CreditorModel> getCreditorList(String dDebtNo, String dContractNo, String dDebtTransferredDateStart, String dDebtTransferredDateEnd, Integer dDebtStatus, Integer dMatchedStatus, Integer perPage, Integer start);

    List<Object[]> getCreditorSum(String dDebtNo, String dContractNo, String dDebtTransferredDateStart, String dDebtTransferredDateEnd, Integer dDebtStatus, Integer dMatchedStatus, Integer perPage, Integer start);
}
