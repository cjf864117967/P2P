package cn.itcast.service.user.impl;

import cn.itcast.dao.IUserDao;
import cn.itcast.domain.user.UserModel;
import cn.itcast.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceimpl implements UserService {
    @Autowired
    IUserDao iUserDao;

    @Override
    public UserModel findByUserName(String username) {
        return iUserDao.findByUsername(username);
    }

    @Override
    public UserModel findByPhone(String phone) {
        return iUserDao.findByPhone(phone);
    }

    @Override
    public boolean addUser(UserModel user) {
        UserModel u = iUserDao.save(user);
        return u!=null;
    }

    @Override
    public UserModel login(String str, String pwd) {
        return iUserDao.login(str,pwd);
    }

    @Override
    public UserModel findById(int userId) {
        return iUserDao.findOne(userId);
    }

    @Override
    public void updatePhoneStatus(String phone, int id) {
        iUserDao.updatePhoneStatus(phone,id);
    }

    @Override
    public void updateRealName(String realName, String identity, int id) {
        iUserDao.updateRealName(realName,identity,id);
    }

    @Override
    public void addEmail(String email, int parseInt) {
        iUserDao.addEmail(email,parseInt);
    }

    @Override
    public void updateEmailStatus(int parseInt) {
        iUserDao.updateEmailStatus(parseInt);
    }
}
