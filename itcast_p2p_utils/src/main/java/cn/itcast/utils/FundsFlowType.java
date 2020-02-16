package cn.itcast.utils;

public class FundsFlowType {
    /**回款--收**/
    public static final int REPAY = 11001;
    /**到期赎回--收**/
    public static final int DUE_REDEEM = 11001;
    /**回款类型**/
    public static final int REPAY_TYPE = 106;
    /**回款--交易成功**/
    public static final int REPAY_SUCCESS = 10601;
    /**投资**/
    public static final int INVEST = 11002;
    /**投资类型**/
    public static final int INVEST_TYPE = 133;
    /**投资--交易成功**/
    public static final int INVEST_SUCCESS = 10404;
    /**投资--交易失败*/
    public static final int INVEST_FAIL = 10400;
    /**到期赎回类型**/
    public static final int DUE_REDEEM_TYPE = 147;
    /**到期赎回--交易成功**/
    public static final int DUE_REDEEM_SUCCESS = 12401;
    /**清算结果--成功**/
    public static final int SETTLE_SUCCESS = 12201;
    /**清算结果--失败**/
    public static final int SETTLE_FAIL  = 12200;
    /**清算结果--初始化**/
    public static final int SETTLE_INIT  = 12299;
    /**清算结果--未执行**/
    public static final int SETTLE_WAIT  = 12289;
    /**清算结果--未执行**/
    public static final String SETTLE_WAIT_NAME  = "未执行";
    /**清算结果--前台是否可以点击标记**/
    public static final int SETTLE_FLAG  = 1;//1为可以点击
    /**资金队列--未锁定**/
    public static final int FUND_NOT_LOCK  = 10901;
    /**资金队列--锁定**/
    public static final int FUND_IS_LOCK  = 10905;
    /**红包类型**/
    public static final int RED_ENVELOPE_TYPE = 149;
    /**代金券**/
    public static final int VOUCHER_TYPE = 158;
    /**交易成功**/
    public static final int TRANS_SUCCESS = 1;
    /**收**/
    public static final int RECEIPT = 11001;
    /**付**/
    public static final int PAYMENT= 11002;
}
