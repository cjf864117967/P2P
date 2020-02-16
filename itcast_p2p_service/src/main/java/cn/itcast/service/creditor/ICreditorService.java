package cn.itcast.service.creditor;

import cn.itcast.domain.creditor.CreditorModel;

import java.util.List;
import java.util.Map;

public interface ICreditorService {
    void addMultiple(List<CreditorModel> cms);

    List<CreditorModel> getCreditorList(Map<String, Object> map);

    List<Object[]> getCreditorSum(Map<String, Object> map);

    void checkCreditor(String[] id);
}
