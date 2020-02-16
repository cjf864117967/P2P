package cn.itcast.service.admin.impl;

import cn.itcast.dao.AdminDAO;
import cn.itcast.domain.action.AdminModel;
import cn.itcast.service.admin.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IAdminServiceimpl implements IAdminService {
    @Autowired
    AdminDAO adminDAO;

    @Override
    public AdminModel login(String username, String password) {
        return adminDAO.login(username,password);
    }
}
