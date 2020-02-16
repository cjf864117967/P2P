package cn.itcast.dao;

import cn.itcast.domain.matchManagement.WeigthRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RuleDAO extends JpaRepository<WeigthRule,Integer> {
    @Query(value = "select rule from WeigthRule rule where rule.weigthType=?1")
    WeigthRule getRuleByType(int i);
}
