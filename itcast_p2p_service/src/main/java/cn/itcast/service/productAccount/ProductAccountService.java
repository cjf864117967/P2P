package cn.itcast.service.productAccount;

import cn.itcast.domain.accountLog.AccountLog;
import cn.itcast.domain.product.ProductAccount;
import cn.itcast.domain.productAccount.FundingNotMatchedModel;
import cn.itcast.domain.user.UserAccountModel;
import org.springframework.data.domain.Page;

import java.util.Date;

public interface ProductAccountService {
    int addProductAccount(ProductAccount productAccount, UserAccountModel userAccount, AccountLog accountLog, FundingNotMatchedModel fnm);

    Page<ProductAccount> findByPage(int cp, int currentNum);

    Page<ProductAccount> findByPage(int userId, Date start, Date end, int parseInt, int i, int currentNum);

//    void findByPage(int cp, int currentNum);
}
