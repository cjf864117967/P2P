package cn.itcast.dao;

import cn.itcast.domain.user.UserAccountModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserAccoutModelDao extends JpaRepository<UserAccountModel,Integer> {
    UserAccountModel findByUserId(int userId);

    @Modifying
    @Query("update UserAccountModel account set account.balance = account.balance + ?1, account.total=account.total + ?1 where account.userId = ?2 ")
    void updateAccountByUserId(Double chargeMoney, int userId);

    UserAccountModel getUserAccountByUserId(int userId);



    @Modifying
    @Query(value="update UserAccountModel account set account.inverstmentW = ?1, account.balance = ?2, "
            + " account.recyclingInterest = ?3, account.addCapitalTotal = ?4, account.capitalTotal = ?5 "
            + " where account.userId = ?6 and account.balance > ?2 ")
    int updateNewInvestmentUserAccount(double inverstmentW, double balance, double recyclingInterest, double addCapitalTotal, double capitalTotal, Integer userId);
}
