package cn.itcast.service.productAccount.impl;

import cn.itcast.dao.AccountLogDAO;
import cn.itcast.dao.FundingNotMatchedModelDAO;
import cn.itcast.dao.ProductAccountDAO;
import cn.itcast.dao.UserAccoutModelDao;
import cn.itcast.domain.accountLog.AccountLog;
import cn.itcast.domain.product.ProductAccount;
import cn.itcast.domain.productAccount.FundingNotMatchedModel;
import cn.itcast.domain.user.UserAccountModel;
import cn.itcast.service.productAccount.ProductAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ProductAccountServiceimpl implements ProductAccountService {

    @Autowired
    UserAccoutModelDao userAccoutModelDao;

    @Autowired
    ProductAccountDAO productAccountDAO;

    @Autowired
    AccountLogDAO accountLogDAO;
    @Autowired
    FundingNotMatchedModelDAO fundingNotMatchedModelDAO;
    @Override
    public int addProductAccount(ProductAccount pa, UserAccountModel ua, AccountLog al, FundingNotMatchedModel fnm) {
        int i = userAccoutModelDao.updateNewInvestmentUserAccount(ua.getInverstmentW(),
                ua.getBalance(), ua.getRecyclingInterest(),
                ua.getAddCapitalTotal(), ua.getCapitalTotal(), ua.getUserId());
        if (i == 0) {
            return 0;
        }
        productAccountDAO.save(pa);
        int pId = pa.getpId();
        al.setpId(pId);
        fnm.setfInvestRecordId(pId); // 投资记录ID
        accountLogDAO.save(al);
        fundingNotMatchedModelDAO.save(fnm);
        return 1;
    }

    @Override
    public Page<ProductAccount> findByPage(int cp, int currentNum) {
        //pageable就是他的分页条件
        Pageable pageable = new PageRequest(cp,currentNum);
        Page<ProductAccount> page = productAccountDAO.findAll(pageable);
        return page;
    }
    //查询投资信息，有条件
    @Override
        public Page<ProductAccount> findByPage(final int userId, final Date start, final Date end, final int status, int i, int currentNum) {
        Pageable pageable = new PageRequest(i,currentNum);
        Page<ProductAccount> page = productAccountDAO.findAll(new Specification<ProductAccount>(){
            @Override
            public Predicate toPredicate(Root<ProductAccount> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<Long> pUid = root.get("pUid");
                Path<Integer> pStatus = root.get("pStatus");
                Path<Date> pBeginDate = root.get("pBeginDate");
                Predicate p1 = cb.equal(pUid, userId);
                Predicate p2 = cb.equal(pStatus, status);
                List<Predicate> ps = new ArrayList<>();
                ps.add(p1);
                ps.add(p2);
                if(start != null){
                    Predicate p3 = cb.greaterThanOrEqualTo(pBeginDate,start);
                    ps.add(p3);
                }
                if(end != null){
                    Predicate p4 = cb.lessThanOrEqualTo(pBeginDate,end);


                    ps.add(p4);
                }

                query.where(ps.toArray(new Predicate[ps.size()]));

                return null;
            }
        },pageable);

        return page;
    }
}
