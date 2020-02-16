package cn.itcast.dao;

import cn.itcast.domain.user.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface IUserDao extends JpaRepository<UserModel,Integer> {
    UserModel findByUsername(String username);

    UserModel findByPhone(String phone);
    @Query("select u from UserModel u where u.username=?1 and u.password=?2")
    UserModel login(String str, String pwd);
    @Modifying
    @Query("update UserModel u set u.phone=?1,u.phoneStatus=1 where u.id=?2")
    void updatePhoneStatus(String phone, int id);

    @Modifying
    @Query("update UserModel u set u.realName=?1,u.identity=?2,u.realNameStatus=1 where u.id=?3")
    void updateRealName(String realName, String identity, int id);
    @Modifying
    @Query("update UserModel u set u.email =?1 where u.id=?2 ")
    void addEmail(String email, int parseInt);

    @Modifying
    @Query("update UserModel u set u.emailStatus=1 where u.id=?1")
    void updateEmailStatus(int parseInt);
}
