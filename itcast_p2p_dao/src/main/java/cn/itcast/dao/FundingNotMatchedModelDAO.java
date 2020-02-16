package cn.itcast.dao;

import cn.itcast.domain.productAccount.FundingNotMatchedModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FundingNotMatchedModelDAO extends JpaRepository<FundingNotMatchedModel,Integer> {
}
