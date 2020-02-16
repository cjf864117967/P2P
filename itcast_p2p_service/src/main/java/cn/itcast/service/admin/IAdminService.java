package cn.itcast.service.admin;

import cn.itcast.domain.action.AdminModel;

public interface IAdminService {
    AdminModel login(String username, String password);
}
