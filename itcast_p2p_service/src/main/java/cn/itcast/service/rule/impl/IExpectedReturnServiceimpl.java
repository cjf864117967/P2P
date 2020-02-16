package cn.itcast.service.rule.impl;

import cn.itcast.dao.IExpctedReturnDAO;
import cn.itcast.domain.productAccount.ExpectedReturn;
import cn.itcast.service.rule.IExpectedReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IExpectedReturnServiceimpl implements IExpectedReturnService {
    @Autowired
    IExpctedReturnDAO iExpctedReturnDAO;
    @Override
    public void addExpectedReturn(ExpectedReturn er) {
        iExpctedReturnDAO.save(er);



    }
}
