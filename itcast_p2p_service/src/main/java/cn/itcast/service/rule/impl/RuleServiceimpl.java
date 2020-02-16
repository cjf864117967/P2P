package cn.itcast.service.rule.impl;

import cn.itcast.dao.RuleDAO;
import cn.itcast.domain.matchManagement.WeigthRule;
import cn.itcast.service.rule.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RuleServiceimpl implements RuleService {
    @Autowired
    RuleDAO ruleDAO;
    @Override
    public WeigthRule getRuleByType(int i) {
        return ruleDAO.getRuleByType(i);
    }
}
