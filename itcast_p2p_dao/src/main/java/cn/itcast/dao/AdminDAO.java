package cn.itcast.dao;

import cn.itcast.domain.action.AdminModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdminDAO extends JpaRepository<AdminModel,Integer> {
    @Query("select a from AdminModel a where a.username=?1 and a.password=?2")
    public AdminModel login(String username,String password);
}
