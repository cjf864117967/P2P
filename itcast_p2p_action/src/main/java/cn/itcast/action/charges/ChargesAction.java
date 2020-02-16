package cn.itcast.action.charges;

import cn.itcast.action.common.BaseAction;
import cn.itcast.action.filter.GetHttpResponseHeader;
import cn.itcast.cache.BaseCacheService;
import cn.itcast.domain.accountLog.AccountLog;
import cn.itcast.domain.matchManagement.WeigthRule;
import cn.itcast.domain.product.Product;
import cn.itcast.domain.product.ProductAccount;
import cn.itcast.domain.product.ProductEarningRate;
import cn.itcast.domain.productAccount.ExpectedReturn;
import cn.itcast.domain.productAccount.FundingNotMatchedModel;
import cn.itcast.domain.user.UserAccountModel;
import cn.itcast.service.bank.BankCardService;
import cn.itcast.service.charge.ChargeService;
import cn.itcast.service.product.IProductService;
import cn.itcast.service.productAccount.ProductAccountService;
import cn.itcast.service.rule.IExpectedReturnService;
import cn.itcast.service.rule.RuleService;
import cn.itcast.service.user.UserAccountModelService;
import cn.itcast.service.userverification.INotificationService;
import cn.itcast.utils.*;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Namespace("/charges")
@Controller
@Scope("prototype")
public class ChargesAction extends BaseAction {
    @Autowired
    BaseCacheService baseCacheService;


    @Autowired
    BankCardService bankCardService;
    @Autowired
    ChargeService chargeService;
    @Autowired
    UserAccountModelService accountModelService;


    @Autowired
    IProductService iProductService;

    @Autowired
    RuleService ruleService;

    @Autowired
    ProductAccountService productAccountService;

    @Autowired
    IExpectedReturnService iExpectedReturnService;
    @Autowired
    INotificationService iNotificationService;
    @Action("charge")
    public void charge(){
//        //得表请求参数
//        String chargeMoney = this.getRequest().getParameter("chargeMoney");
//        //根据当前用户id，得到用户的绑定的银行卡号
//        String token = this.getRequest().getHeader("token");
//        Map<String, Object> hmap = baseCacheService.getHmap(token);
//        int userId = (int) hmap.get("id");
//        //查询银行卡号
//        BankCardInfo bci = bankCardService.findByUserId(userId);
//        String bankCardNum = bci.getBankCardNum();
//        chargeService.recharge(bankCardNum,Double.parseDouble(chargeMoney));
        try{
            String token = GetHttpResponseHeader.getHeadersInfo(getRequest());
            if (StringUtils.isEmpty(token)) { // 判断token是否为空
                getResponse().getWriter().write(Response.build().setStatus("13").toJSON());
                return;
            }

            Map<String, Object> hmap = baseCacheService.getHmap(token);
            if (hmap == null ||hmap.isEmpty()) {
                getResponse().getWriter().write(Response.build().setStatus("15").toJSON());
                return;
            }
            int userId = (Integer) hmap.get("id");	//通过产品名称获取用户id
            String chargeMoney = getRequest().getParameter("chargeMoney");
            accountModelService.charge(userId, Double.valueOf(chargeMoney), 0);
            getResponse().getWriter().write(Response.build().setStatus("1").toJSON());
        }catch(Exception e){
            try {
                getResponse().getWriter().write(Response.build().setStatus("0").toJSON());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
//            logger.error(e.getMessage());
        }
    }

    /**
     *
     * @Title: addMounthTake
     * 方法描述：月取计划
     * @version 1.0
     * @param pProductId 产品id
     * @param pAmount 购买金额
     * @param pDeadline 购买期限
     * @param pExpectedAnnualIncome 预期年化收益
     * @param pMonthInterest 每月盈取利息
     * @param pMonthlyExtractInterest 每月提取利息
     * @return void    插入状态，成功：1、失败：0
     * @throws
     *
     */
    @Action("addMayTake")
    public synchronized void addMayTake() {
        try {
            //获取请求信息
            String pDeadline = getRequest().getParameter("pDeadline");
            String pProductId = getRequest().getParameter("pProductId");
            String pAmount = getRequest().getParameter("pAmount");
            String pExpectedAnnualIncome = getRequest().getParameter("pExpectedAnnualIncome");
            String pMonthInterest = getRequest().getParameter("pMonthInterest");
            String pMonthlyExtractInterest = getRequest().getParameter("pMonthlyExtractInterest");

            String token = this.getRequest().getHeader("token");
            // 判断token是否为空
            if (StringUtils.isEmpty(token)) { // 判断token是否为空
                getResponse().getWriter().write(Response.build().setStatus("13").toJSON());
                return;
            }
            Map<String, Object> hmap = baseCacheService.getHmap(token);
            if(hmap==null ||hmap.isEmpty()){//判断用户是否为空
                getResponse().getWriter().write(Response.build().setStatus("15").toJSON());
                return;
            }
            int userId = (Integer) hmap.get("id");
            String name =  (String) hmap.get("userName");
            Integer mainId = null; //账户的用户ID
            Integer earntype = null; //收益率类型
            Integer ptype = null; //产品类型
            String productName = null;
            Integer padvancetype = null; //提前赎回率类型
            Double pAdvanceRedemption = null;//提前赎回率
            Double uBalance = null; //账户可用余额
            Double uRecyclingInterest = null;//月取总额
            Double uInvestmentA = null;//已投资总额
            Double expectedAnnualIncome = null;//预期年化收益
            Double monthlyExtractInterest = null;//每月提取利息
            Double monthInterest = null;//每月盈取利息
            Double inverstmentW = null;//总计代收本金
            Double interestTotal = null;//总计代收利息
            Date date = new Date();//获取当前时间
            //获取结束日期
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH,+Integer.parseInt(pDeadline));
            Date endate = calendar.getTime();
            //获取预期年化收益
            ProductEarningRate rate = accountModelService.selectYearInterest(Integer.parseInt(pProductId),Integer.parseInt(pDeadline));
            //获取每月盈取利息
            String monthWinInterestStr = BigDecimalUtil.monthWinInterest(pAmount, Double.toString(rate.getIncomeRate()));
            //校验
            // 购买金额名不能为空
            if (StringUtils.isEmpty(pProductId)) {
                getResponse().getWriter().write(Response.build().setStatus("18").toJSON());
                return;
            }
            // 购买金额名不能为空
            if (StringUtils.isEmpty(pAmount)) {
                getResponse().getWriter().write(Response.build().setStatus("19").toJSON());
                return;
            }
            // 购买金额最小值大于100
            if (Double.parseDouble(pAmount)<100) {
                getResponse().getWriter().write(Response.build().setStatus("96").toJSON());
                return;
            }
            // 购买金额为100的整数倍
            if (Double.parseDouble(pAmount)%100!=0) {
                getResponse().getWriter().write(Response.build().setStatus("97").toJSON());
                return;
            }

            UserAccountModel account = accountModelService.getAccountByUserId(userId);
            if(account==null){
//                logger.debug(account+"数据为空");
                getResponse().getWriter().write(Response.build().setStatus("2").toJSON());
                return;
            }else{
                mainId = account.getUserId(); //获取账户的用户id
                uBalance = account.getBalance(); //获取账户可用余额
                uRecyclingInterest = account.getRecyclingInterest();//月取总额
                uInvestmentA = account.getInverstmentA();//已投资总额
                inverstmentW = account.getInverstmentW();//总计代收本金
                interestTotal = account.getInterestTotal();//总计代收利息
            }

            Product product = iProductService.getById(Long.valueOf(pProductId));
            if(product==null){
//                logger.debug(product+"数据为空");
                getResponse().getWriter().write(Response.build().setStatus("2").toJSON());
                return;
            }else{
                earntype = product.getEarningType(); //获取收益率类型
                ptype = product.getProTypeId(); //获取产品类型
                padvancetype = product.getEarlyRedeptionType(); //获取提前赎回率类型
                productName = product.getProductName();

            }
            //新投资权重
            WeigthRule newInvestmentWeight = ruleService.getRuleByType(124);
            //资产总额
            Double uTotal = uBalance+inverstmentW+interestTotal;
            // 可用金额不足
            if ("".equals(uBalance) || uBalance<=0.00 || Double.parseDouble(pAmount)-uBalance>0) {
                getResponse().getWriter().write(Response.build().setStatus("17").toJSON());
                return;
            }
            // 账户总额不足
            if ("".equals(uTotal) || uTotal<=0.00 || uBalance-uTotal>0) {
                getResponse().getWriter().write(Response.build().setStatus("17").toJSON());
                return;
            }
            // 期限不能小于十二个月
            if (Integer.parseInt(pDeadline)<12) {
                getResponse().getWriter().write(Response.build().setStatus("95").toJSON());
                return;
            }
            // 预期年化收益为空或与数据库中不等,重新计算
            if (StringUtils.isEmpty(pExpectedAnnualIncome) || Double.parseDouble(pExpectedAnnualIncome)!=rate.getIncomeRate()) {
                expectedAnnualIncome = rate.getIncomeRate();
            } else {
                expectedAnnualIncome = Double.parseDouble(pExpectedAnnualIncome);
            }
            // 每月盈取利息为空或与计算值不等,重新计算
            if (StringUtils.isEmpty(pMonthInterest) || Double.parseDouble(pMonthInterest)!=Double.parseDouble(monthWinInterestStr)) {
                monthInterest = Double.parseDouble(monthWinInterestStr);
            } else {
                monthInterest = Double.parseDouble(pMonthInterest);
            }
            // 每月提取利息为空
            if (pMonthlyExtractInterest==null || "".equals(pMonthlyExtractInterest)) {
                monthlyExtractInterest = 0.00;
            } else {
                monthlyExtractInterest = Double.parseDouble(pMonthlyExtractInterest);
            }
            // 每月提取利息小于每月盈取利息
            if (monthlyExtractInterest>new BigDecimal(Double.parseDouble(monthWinInterestStr)).setScale(2, RoundingMode.DOWN).doubleValue()) {
                getResponse().getWriter().write(Response.build().setStatus("21").toJSON());
                return;
            }
            //到期应回总本息
            String pEndInvestTotalMoney = BigDecimalUtil.endInvestTotalMoney(pAmount, pDeadline, Double.toString(expectedAnnualIncome), pMonthlyExtractInterest);
            //月取代收利息
            String mayReplaceInterestIncome = BigDecimalUtil.sub(pEndInvestTotalMoney.toString(), pAmount).toString();
            //交易后金额
            BigDecimal endTotal = BigDecimalUtil.sub(uBalance.toString(), pAmount);
            //账户可用余额
            BigDecimal balance = BigDecimalUtil.sub(uBalance.toString(), pAmount);
            //月取总额
            BigDecimal recyclingInterest = BigDecimalUtil.add(uRecyclingInterest.toString(), pAmount);
            //已投资总额
            BigDecimal investmentA = BigDecimalUtil.add(uInvestmentA.toString(), pAmount);
            //总计代收本金
            BigDecimal inverstmentw = BigDecimalUtil.add(inverstmentW.toString(), pAmount);
            //总计代收利息
            String interesttotal = BigDecimalUtil.add(interestTotal.toString(), mayReplaceInterestIncome).toString();
            //随机码
            String randomNO = RandomNumberUtil.randomNumber(date);

            //用户账户表
            ProductAccount productAccount = new ProductAccount();
            productAccount.setpUid(Long.parseLong(userId+"")); //用户ID
            productAccount.setpSerialNo("TZNO"+randomNO);//投资编号
            productAccount.setpProductId(Long.parseLong(pProductId));//产品ID
            productAccount.setpProductType(ptype);//产品类型
            productAccount.setpProductName(productName);
            productAccount.setpEarningsType(earntype);//收益率类型
            productAccount.setpAdvanceRedemption(pAdvanceRedemption);//提前赎回利率
            productAccount.setpAmount(Double.parseDouble(pAmount));//金额
            productAccount.setpDeadline(Integer.parseInt(pDeadline));//选择期限
            productAccount.setpExpectedAnnualIncome(expectedAnnualIncome);//预期年化收益
            productAccount.setpMonthInterest(monthInterest);//每月盈取利息
            productAccount.setpMonthlyExtractInterest(monthlyExtractInterest);//每月提取利息
            productAccount.setpBeginDate(date);//开始时间
            productAccount.setpEndDate(endate);//结束日期
            productAccount.setpStatus(InvestStatus.WAIT_TO_MATCH);//投资状态
            productAccount.setpAvailableBalance(Double.parseDouble(pAmount));//可用余额
            productAccount.setpEndInvestTotalMoney(Double.valueOf(pEndInvestTotalMoney));//到期应回总本息
            productAccount.setpProspectiveEarnings(0.0);//预期收益
            productAccount.setpCurrentMonth(1);//当前期
            productAccount.setpCurrentRealTotalMoney(0.0);//当前期实回总本息
            productAccount.setpFrozenMoney(0.0);//冻结金额
            productAccount.setpEarnedInterest(0.0);//以赚取利息
            productAccount.setpDeductInterest(0.0);//扣去利息
            productAccount.setpNotInvestMoney(0.0);//未投资金额

            //修改用户账户信息
            UserAccountModel userAccount = new UserAccountModel();
            userAccount.setUserId(userId);//用户id
            userAccount.setBalance(balance.doubleValue());//账户可用余额
            userAccount.setInverstmentW(inverstmentw.doubleValue());//总计代收本金
            userAccount.setInterestTotal(BigDecimalUtil.formatNdecimal(new BigDecimal(interesttotal),2).doubleValue());//总计代收利息
            userAccount.setRecyclingInterest(recyclingInterest.doubleValue());//月取总额
            userAccount.setInterestA(investmentA.doubleValue());//已投资总额

            //用户交易记录表 -- 类似交易记录日志
            AccountLog accountLog = new AccountLog();
            accountLog.setaUserId(userId);//用户id
            accountLog.setaMainAccountId(mainId); //主账户id
            accountLog.setaReceiveOrPay(InvestTradeType.PAY);//收付
            accountLog.setaCurrentPeriod(1);//当前期
            accountLog.setaBeforeTradingMoney(uBalance);//交易前金额
            accountLog.setaAmount(Double.parseDouble(pAmount));//交易金额
            accountLog.setaAfterTradingMoney(endTotal.doubleValue()); //交易后金额
            accountLog.setaDate(date);//时间
            accountLog.setaType(FundsFlowType.INVEST_TYPE);//交易类型
            accountLog.setaTransferStatus(FundsFlowType.INVEST_SUCCESS);//交易状态
            accountLog.setaTransferSerialNo("LSNO"+randomNO);//交易流水号
            accountLog.setaDescreption("月取计划"+"TZNO"+randomNO);//交易详情

            //投资的时候往待匹配的表中插入相应记录
            FundingNotMatchedModel fnm = new FundingNotMatchedModel();
            fnm.setfNotMatchedMoney(Double.parseDouble(pAmount));//待匹配金额
            fnm.setfFoundingType(124);//资金类型 -- 124  新增投资
            fnm.setfFoundingWeight(newInvestmentWeight.getWeigthValue());//资金权重
            fnm.setfIsLocked(10901);//是否锁定  --- 待匹配
            fnm.setfCreateDate(date);//创建时间

            int result = productAccountService.addProductAccount(productAccount, userAccount, accountLog, fnm);
            if (result == 0) {
                getResponse().getWriter().write(Response.build().setStatus("17").toJSON());
                return;
            }

            //获取月取预计收益值   数据存入T_USER_Expected_return表中
            ExpectedReturn er = new ExpectedReturn();
            for (int k = 0; k < Integer.parseInt(pDeadline); k++) {
                String yearMonth = TimestampUtils.nextMonth(date.getYear(),date.getMonth(), k);
                er.setExpectedDate(yearMonth);//年月
                er.setExpectedMoney(monthInterest);//月盈利
                er.setUserId(userId);//用户id
                er.setCreateDate(date);//当前时间
                er.setProductId(Integer.parseInt(pProductId));//产品id
                iExpectedReturnService.addExpectedReturn(er);
            }
            //信息
            String message = "【传智P2P】亲爱的"+name+"，您已加入“月取计划”投资编号TZNO"+randomNO+"，您的投资款¥"+Double.parseDouble(pAmount)+"已在您的账户冻结，预计将于明日开始计息。";
            iNotificationService.sendMessage(String.valueOf(userId), "投资成功", message);
             getResponse().getWriter().write(Response.build().setStatus("1").toJSON());
        } catch (Exception e) {
//            logger.error("新增月取计划失败");
            try {
                getResponse().getWriter().write(Response.build().setStatus("0").toJSON());
            } catch (IOException e1) {
//                logger.error(e1.getMessage());
            }
        }
    }
    //分页查询投资信息
    @Action("ProductAccountBuying")
    public void ProductAccountBuying(){
        this.getResponse().setCharacterEncoding("utf-8");
        String token = this.getRequest().getHeader("token");
        //得到页码
        String currentPage = this.getRequest().getParameter("currentPage");
        String status = this.getRequest().getParameter("status");
        String startDate = this.getRequest().getParameter("startDate");
        String endDate = this.getRequest().getParameter("endDate");
        int userId = (int) baseCacheService.getHmap(token).get("id");
        try {
            Date start = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(StringUtils.isNotBlank(startDate)){
                start = sdf.parse(startDate);
            }

            Date end = null;
            if(StringUtils.isNotBlank(endDate)){
                end = sdf.parse(endDate);
            }
            //处理分页信息
            int cp = Integer.parseInt(currentPage);
            int currentNum = 2;
            Page<ProductAccount> page = productAccountService.findByPage(userId,start,end,Integer.parseInt(status),cp-1,currentNum);
            //通过page对象获取list
            List<ProductAccount> pas = page.getContent();
            long totalElements = page.getTotalElements();//总条数
            int totalPages = page.getTotalPages();//总页数
            //向浏览器响应数据
            this.getResponse().getWriter().write(Response.build().setStatus("1").setData(pas).setTotal(totalElements+"").toJSON());
        }catch (Exception e){
            e.printStackTrace();
        }



    }
}
