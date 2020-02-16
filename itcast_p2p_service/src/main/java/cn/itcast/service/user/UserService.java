package cn.itcast.service.user;

import cn.itcast.domain.user.UserModel;

public interface UserService {
    UserModel findByUserName(String username);

    UserModel findByPhone(String phone);

    boolean addUser(UserModel user);

    UserModel login(String str, String pwd);

    UserModel findById(int userId);

    void updatePhoneStatus(String phone, int id);

    void updateRealName(String realName, String identity, int id);

    void addEmail(String email, int parseInt);

    void updateEmailStatus(int parseInt);
}
