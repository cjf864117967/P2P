package cn.itcast.service.creditor.impl;

import cn.itcast.dao.ICreditorDAO;
import cn.itcast.domain.creditor.CreditorModel;
import cn.itcast.service.creditor.ICreditorService;
import cn.itcast.utils.ClaimsType;
import cn.itcast.utils.creditor.CreditorDao4Sql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ICreditorServiceimpl implements ICreditorService {
    @Autowired
    ICreditorDAO iCreditorDAO;

    @Autowired
    CreditorDao4Sql creditorDao4Sql;

    @Override
    public void addMultiple(List<CreditorModel> cms) {
        iCreditorDAO.save(cms);
    }

    @Override
    public List<CreditorModel> getCreditorList(Map<String, Object> map) {
        return creditorDao4Sql.getCreditorList((String)map.get("dDebtNo"), (String)map.get("dContractNo"),
                (String)map.get("dDebtTransferredDateStart"), (String)map.get("dDebtTransferredDateEnd"), (Integer)map.get("dDebtStatus"),
                (Integer)map.get("dMatchedStatus"), (Integer)map.get("perPage"), (Integer)map.get("start"));
    }

    @Override
    public List<Object[]> getCreditorSum(Map<String, Object> map) {
        return creditorDao4Sql.getCreditorSum((String)map.get("dDebtNo"), (String)map.get("dContractNo"),
                (String)map.get("dDebtTransferredDateStart"), (String)map.get("dDebtTransferredDateEnd"), (Integer)map.get("dDebtStatus"),
                (Integer)map.get("dMatchedStatus"), (Integer)map.get("perPage"), (Integer)map.get("start"));
    }

    @Override
    public void checkCreditor(String[] id) {
        for (int i = 0; i < id.length; i++) {
            CreditorModel creditor = iCreditorDAO.findOne(Integer.parseInt(id[i].trim()));
            if(creditor != null){
                //查找到

                //修改债权的状态
                creditor.setDebtStatus(ClaimsType.CHECKED);
            }
        }
    }
}
