package cn.itcast.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;



public class BigDecimalUtil {
	
	/**
	 *@param transferredMoney
	 *@param expectedannual
	 *@param transferredperiod
	 *@return
	 *@description  月定投 本金加利息 年
	 */
	public static BigDecimal getOldMoneyAndInterest(double transferredMoney,double expectedannual,double transferredperiod){
		BigDecimal monthInterest=div(expectedannual, 12.0,8);
		BigDecimal monthInterestPercent=div(monthInterest.doubleValue(), 100,6);
		double yearMoney=mul(transferredMoney, transferredperiod).doubleValue();
		double yearMoneyInterest=mul(yearMoney, monthInterestPercent.doubleValue()).doubleValue();
		BigDecimal monthAndI=add(yearMoneyInterest, transferredMoney);
		return monthAndI.setScale(2, BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * 加法运算
	 * @param num1
	 * @param num2
	 * @return
	 */
	public static BigDecimal add(String num1,String num2){
		BigDecimal b1=new BigDecimal(num1);
		BigDecimal b2=new BigDecimal(num2);
		BigDecimal addNum = b1.add(b2);
		return addNum;
	}
	
/**
 * 将10日内充值的金额与充值后投资的金额进行对比，
 * 若前者大于后者，则可提现金额应为当前余额-10日内充值的金额+投资金额；
 * 若前者小于后者，则可提现金额=当前余额
 * @param balance
 * @param chargeMoney
 * @param investMoneyAll
 * @return
 */
	public static BigDecimal getAvailableBal(double balance,double chargeMoney,double investMoneyAll){
		BigDecimal bal=new BigDecimal(balance);
		BigDecimal charge=new BigDecimal(chargeMoney);
		BigDecimal invest=new BigDecimal(investMoneyAll);
		if(charge.compareTo(invest)==1){
			BigDecimal add = bal.subtract(charge).add(invest);
			return add;
		}else{
			return bal;
		}
		
	}
	
	/**
	 * 判断手续费和提现金额的大小，并且得到实际的提现金额
	 * @param balance
	 * @param cost
	 * @return
	 */
	public static BigDecimal getBalan(String availableBalancence,String balance){
		BigDecimal availableBalance=new BigDecimal(availableBalancence);
		BigDecimal balan=new BigDecimal(balance);
		if(availableBalance.compareTo(balan)==-1 ){
			BigDecimal flag=new BigDecimal("-1");
			System.out.println("flag....."+flag.toString());
			return flag;
		}else{
			BigDecimal flag=new BigDecimal("1");
			System.out.println("flag2....."+flag.toString());
			return flag;
		}
		
	}
	
	/**
	 * 判断手续费和提现金额的大小，并且得到实际的提现金额
	 * @param balance
	 * @param cost
	 * @return
	 */
	public static BigDecimal getExtract(String balance,String cost){
		System.out.println("-----sysout ---balance"+balance);
		BigDecimal bal=new BigDecimal(balance);
		BigDecimal co=new BigDecimal(cost);
		BigDecimal sub = bal.subtract(co);
		System.out.println("sub......"+sub);
		return sub;
		}
		
	
/*	public static BigDecimal getExtract(String balance,String cost){
		System.out.println("-----sysout ---balance"+balance);
		BigDecimal bal=new BigDecimal(balance);
		BigDecimal co=new BigDecimal(cost);
		if(bal.compareTo(co)==-1 || bal.compareTo(co)==0){
			BigDecimal flag=new BigDecimal("-1");
			System.out.println("flag---->"+flag);
			return flag;
		}else{
			BigDecimal sub = bal.subtract(co);
			System.out.println("sub-----"+sub);
			return sub;
		}
		
	}*/
	
	/**
	 * 	//计算金额时，常量相乘，如：100*24  或 100*12
	 * @param num1
	 * @param num2
	 * @author libeibei
	 */
	public static BigDecimal numConstant(String num1,String num2){
		BigDecimal b1=new BigDecimal(num1);
		BigDecimal b2=new BigDecimal(num2);
		BigDecimal mulConstant = b1.multiply(b2);
		return mulConstant;
	}
	
	/**
	 * 每月赢取利息=投资金额*(预期年化利率/12)  (月取计划)  用
	 * @param account
	 * @param yearInterest
	 */
	public static String monthWinInterest(String account,String yearInterest){
		BigDecimal accountNum=new BigDecimal(account);
		BigDecimal yearInterestNum=new BigDecimal(yearInterest);
		String monthWinInterestStr = String.valueOf(format2decimal(accountNum.multiply(yearInterestNum.divide(BigDecimalUtil.numConstant("12", "100"),6,BigDecimal.ROUND_HALF_UP))));
		return monthWinInterestStr;
	}
	
	public static String monthWin(String account,String yearInterest){
		BigDecimal accountNum=new BigDecimal(account);
		BigDecimal yearInterestNum=new BigDecimal(yearInterest);
	//	String monthWinInterestStr = accountNum.multiply(yearInterestNum).divide(BigDecimalUtil.numConstant("12", "100"),2,BigDecimal.ROUND_DOWN).toString();
		String monthWinInterestStr = String.valueOf(format2decimal(accountNum.multiply(yearInterestNum.divide(BigDecimalUtil.numConstant("12", "100"),6,BigDecimal.ROUND_HALF_UP))));
		System.out.println("monthWinInterestStr......."+monthWinInterestStr);
		return monthWinInterestStr;
	}
	
	
	
	/**
	 * 预期收益  (月乘计划)=投资金额*(预期年化利率/12)*投资月数  用
	 * @param account
	 * @param endMonthNum
	 * @param yearInterest
	 * @return
	 */
	public static String expectWinMoney(String account,String endMonthNum,String yearInterest){
		BigDecimal accountM=new BigDecimal(account);
		BigDecimal yearInterestNum=new BigDecimal(yearInterest);
		BigDecimal endMonth=new BigDecimal(endMonthNum);
		//String expectWinMoney = accountM.multiply(yearInterestNum).multiply(endMonth).divide(BigDecimalUtil.numConstant("100", "12"),2,BigDecimal.ROUND_HALF_UP).toString();
		//String expectWinMoney = String.valueOf(format2decimal(accountM.multiply(yearInterestNum.multiply(endMonth).divide(BigDecimalUtil.numConstant("100", "12"),6,BigDecimal.ROUND_HALF_UP))));
		String expectWinMoney = String.valueOf(format2decimal(accountM.multiply(yearInterestNum.divide(BigDecimalUtil.numConstant("100", "12"),6,BigDecimal.ROUND_HALF_UP).multiply(endMonth))));
		System.out.println("expectWinMoney......"+expectWinMoney);
		return expectWinMoney;
	}

	
	/**
	 * 预期收益  (月存计划) 预期收益计算公式：每期加入金额*年化利率*期数*（期数+1）/24  用
	 * @param account
	 * @param endMonthNum
	 * @param yearInterest
	 * @return
	 */
	public static String expectDeposit(String account,String endMonthNum,String yearInterest){
        BigDecimal accountM=new BigDecimal(account);
		BigDecimal yearInterestNum=new BigDecimal(yearInterest);
		BigDecimal endMonth=new BigDecimal(endMonthNum);
		String expectWinMoney = accountM.multiply(yearInterestNum).multiply(endMonth).multiply(endMonth.add(new BigDecimal(1))).divide(BigDecimalUtil.numConstant("100", "24"),2,BigDecimal.ROUND_HALF_UP).toString();;
		System.out.println("expectWinMoney....."+expectWinMoney);
		return expectWinMoney;
	}
	

	
	/**
	 * 加法
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static BigDecimal add(double d1,double d2){
		BigDecimal b1 = new BigDecimal(Double.valueOf(d1).toString());
		BigDecimal b2 = new BigDecimal(Double.valueOf(d2).toString());
		return b1.add(b2);
	}
	/**
	 * 减法
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static BigDecimal sub(double d1,double d2){
		BigDecimal b1 = new BigDecimal(Double.valueOf(d1).toString());
		BigDecimal b2 = new BigDecimal(Double.valueOf(d2).toString());
		return b1.subtract(b2);
	}
	/**
	 * 乘法
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static BigDecimal mul(double d1,double d2){
		BigDecimal b1 = new BigDecimal(Double.valueOf(d1).toString());
		BigDecimal b2 = new BigDecimal(Double.valueOf(d2).toString());
		return b1.multiply(b2);
	}
	/**
	 * 除法
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static BigDecimal div(double d1,double d2){
		BigDecimal b1 = new BigDecimal(Double.valueOf(d1).toString());
		BigDecimal b2 = new BigDecimal(Double.valueOf(d2).toString());
		return b1.divide(b2);
	}
	
	/**
	 * double转BigDecimal
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static BigDecimal intToBigDecimal(int i){
		return new BigDecimal(Integer.valueOf(i).toString());
	}
	/**
	 * double转BigDecimal
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static BigDecimal doubleToBigDecimal(double d){
		return new BigDecimal(Double.valueOf(d).toString());
	}
	/**
	 * 格式化两位小数，转为double
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static double format2decimal(BigDecimal b){
		return b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	/**
	 * 
	 * 方法描述：月存代收利息（新投资）
	 * 创建人：wangcan   
	 * 创建时间：2015年3月28日 上午11:15:22   
	 * 修改人：wangcan   
	 * 修改时间：2015年3月28日 上午11:15:22   
	 * 修改备注：   
	 * @version 1.0
	 * @param account	购买金额
	 * @param endMonthNum		购买期数
	 * @param yearInterest	预期年化利率
	 * @return    
	 *
	 */ 
	public static String monthDepositInterestCollection(String account,String yearInterest,String endMonthNum){
        BigDecimal accountM=new BigDecimal(account);
		BigDecimal yearInterestNum=new BigDecimal(yearInterest);
		BigDecimal endMonth=new BigDecimal(endMonthNum);
		String monthDepositInterestCollection = accountM.multiply(yearInterestNum.divide(new BigDecimal(100)).divide(new BigDecimal(12),6, BigDecimal.ROUND_HALF_UP)).multiply(endMonth).toString();
		return monthDepositInterestCollection;
	}
	/**
	 * 
	 * 方法描述：月乘代收利息（新投资）
	 * 创建人：wangcan   
	 * 创建时间：2015年3月28日 上午11:16:51   
	 * 修改人：wangcan   
	 * 修改时间：2015年3月28日 上午11:16:51   
	 * 修改备注：   
	 * @version 1.0
	 * @param account	购买金额
	 * @param endMonthNum		购买期数
	 * @param yearInterest	预期年化利率
	 * @return    
	 *
	 */
	public static String takeTheCollectionOfMonthlyInterest(String account,String endMonthNum,String yearInterest){
        BigDecimal accountM=new BigDecimal(account);
		BigDecimal yearInterestNum=new BigDecimal(yearInterest);
		BigDecimal endMonth=new BigDecimal(endMonthNum);
		String expectWinMoney = accountM.multiply(yearInterestNum.divide(new BigDecimal(100)).divide(new BigDecimal(12),6, BigDecimal.ROUND_HALF_UP)).multiply(endMonth).toString();
		return expectWinMoney;
	}
	
/*	public static void main(String[] args) {
		String t = takeTheCollectionOfMonthlyInterest("100", "3", "8.1");
		System.out.println(t);
		System.out.println(BigDecimalUtil.formatNdecimal(new BigDecimal(t), 2));
	}
	
	*/
	
	
	
	/**
	 * 
	 * 方法描述：月取代收利息（新投资）
	 * 创建人：wangcan   
	 * 创建时间：2015年3月28日 上午11:18:17   
	 * 修改人：wangcan   
	 * 修改时间：2015年3月28日 上午11:18:17   
	 * 修改备注：   
	 * @version 1.0
	 * @param account	购买金额
	 * @param endMonthNum		购买期数
	 * @param yearInterest	预期年化利率
	 * @return    
	 *
	 */
	public static String mayReplaceInterestIncome(String account,String endMonthNum,String yearInterest){
        BigDecimal accountM=new BigDecimal(account);
		BigDecimal yearInterestNum=new BigDecimal(yearInterest);
		BigDecimal endMonth=new BigDecimal(endMonthNum);
		String expectWinMoney = accountM.multiply(yearInterestNum.divide(new BigDecimal(100)).divide(new BigDecimal(12),6, BigDecimal.ROUND_HALF_UP)).multiply(endMonth).toString();
		return expectWinMoney;
	}
	
	/**
	 * 格式化N位小数，转为BigDecimal
	 * @param b
	 * @return
	 */
	public static BigDecimal formatNdecimal(BigDecimal b,int n){
		return b.setScale(n,   BigDecimal.ROUND_HALF_UP);
	}
	/**
	 * 
	 * 方法描述：月取到期应会总本息
	 * 创建人：wangcan   
	 * 创建时间：2015年4月8日 下午3:10:04   
	 * 修改人：wangcan   
	 * 修改时间：2015年4月8日 下午3:10:04   
	 * 修改备注：   
	 * @version 1.0
	 * @param account	购买金额
	 * @param endMonthNum		购买期数
	 * @param yearInterest	预期年化利率
	 * @param MonthlyExtractInterest	每月提取利息
	 * @return    
	 *
	 */
	public static String endInvestTotalMoney(String account,String endMonthNum,String yearInterest,String monthlyExtractInterestNum){
		BigDecimal accountM=new BigDecimal(account);
		BigDecimal yearInterestNum=new BigDecimal(yearInterest).divide(new BigDecimal(12),6, BigDecimal.ROUND_HALF_UP);
		BigDecimal monthlyExtractInterest=new BigDecimal(monthlyExtractInterestNum);
		BigDecimal expectwinmoney = 
			     accountM.multiply((new BigDecimal("1").add(yearInterestNum.divide(new BigDecimal(100)))).pow(Integer.parseInt(endMonthNum)))
				 .subtract(monthlyExtractInterest.multiply(((new BigDecimal("1").add(yearInterestNum.divide(new BigDecimal(100)))).pow(Integer.parseInt(endMonthNum)))
				 .subtract(new BigDecimal("1"))).divide(yearInterestNum.divide(new BigDecimal(100)),6, BigDecimal.ROUND_HALF_UP)).add(monthlyExtractInterest.multiply(new BigDecimal(endMonthNum)))
				 ;
		String expectWinMoney = expectwinmoney.setScale(6,   BigDecimal.ROUND_HALF_UP).toString();
		return expectWinMoney;
	}
	
//	public static void main(String[] args) {
//		String endInvestTotalMoney = endInvestTotalMoney("100","12","12.0","0");
//	}
	
	/**
	 * 减法
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static BigDecimal sub(String d1,String d2){
		BigDecimal b1 = new BigDecimal(d1);
		BigDecimal b2 = new BigDecimal(d2);
		return b1.subtract(b2);
	}
	/**
	 * 乘法
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static BigDecimal mul(String d1,String d2){
		BigDecimal b1 = new BigDecimal(d1);
		BigDecimal b2 = new BigDecimal(d2);
		return b1.multiply(b2);
	}
	/**
	 * 除法
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static BigDecimal div(String d1,String d2,int scale){
		if(scale<0){   
            throw new IllegalArgumentException(   
            "The scale must be a positive integer or zero");   
	    }   
	    BigDecimal b1 = new BigDecimal(d1);   
	    BigDecimal b2 = new BigDecimal(d2);   
	    return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP);  
		}
		
	/**   
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指   
     * 定精度，以后的数字四舍五入。   
     * @param v1 被除数   
     * @param v2 除数   
     * @param scale 表示表示需要精确到小数点以后几位。   
     * @return 两个参数的商   
     */   
   public static BigDecimal div(double v1,double v2,int scale){   
           if(scale<0){   
                   throw new IllegalArgumentException(   
                   "The scale must be a positive integer or zero");   
           }   
           BigDecimal b1 = new BigDecimal(Double.toString(v1));   
           BigDecimal b2 = new BigDecimal(Double.toString(v2));   
           return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP);   
   }   

   /**
    * 最大可提现金额计算公式
    * 最大提现金额 = 历史充值累计 – 历史投资成功金额 + 历史投资回款金额 – 历史累计提现金额 – 用户明下付款中金额 - 用户名下投资申请金额
    * @return
    */
   public static String getMaxExtractMoney(double d1,double d2,double d3,double d4,double d5){

	   BigDecimal b1 = new BigDecimal(Double.toString(d1));   
       BigDecimal b2 = new BigDecimal(Double.toString(d2)); 
       BigDecimal b3 = new BigDecimal(Double.toString(d3));   
       BigDecimal b4 = new BigDecimal(Double.toString(d4));
       BigDecimal b5 = new BigDecimal(Double.toString(d5));
    //   BigDecimal b6 = new BigDecimal(Double.toString(d6));
       String maxExtractMoney = b1.subtract(b2).add(b3).subtract(b4).subtract(b5).toString();
       return maxExtractMoney;
   }
   
/*   public static String getMaxExtractMoney(double d1,double d2,double d3,double d4,double d5,double d6){

	   BigDecimal b1 = new BigDecimal(Double.toString(d1));   
       BigDecimal b2 = new BigDecimal(Double.toString(d2)); 
       BigDecimal b3 = new BigDecimal(Double.toString(d3));   
       BigDecimal b4 = new BigDecimal(Double.toString(d4));
       BigDecimal b5 = new BigDecimal(Double.toString(d5));
       BigDecimal b6 = new BigDecimal(Double.toString(d6));
       String maxExtractMoney = b1.subtract(b2).add(b3).subtract(b4).subtract(b5).add(b6).toString();
       return maxExtractMoney;
   }*/
   
   /**
    * 
    * //rate*months+rate/maxDaysOfMonth*天数
    * accountD*(rateD/100)*months
    * @return
    */
   /**
    * 计算 月 利息lbb,针对提取结清，老债权（给吴国青用）
    * //accountD*(rateD/100)*months
    * @param d1 : 投资的金额
    * @param d2 ：月利率
    * @param d3 ：月数
    * @return
    */
   public static double getMonthRateInvest(double d1,double d2,int d3){
	   BigDecimal b1 = new BigDecimal(Double.toString(d1));   
       BigDecimal b2 = new BigDecimal(Double.toString(d2)); 
       BigDecimal b3 = new BigDecimal(Double.toString(d3));   
       double monthRateInvestD= format2decimal(b1.multiply(b2.divide(new BigDecimal(100),6,BigDecimal.ROUND_HALF_UP)).multiply(b3));
       return monthRateInvestD;
   }
   /**
    * accountD*(rateD/100/maxDaysOfMonth)*object
    * @return
    */
   /**
    * 计算  天 利息lbb,针对提取结清，老债权（给吴国青用）
    * @param d1 : 投资的金额
    * @param d2 ：月利率
    * @param d3 ：某个月的天数
    * @param d4 ：某个月算利息的天数
    * @return
    */
   public static double getDayRateInvest(double d1,double d2,int d3,int d4){
	   BigDecimal b1 = new BigDecimal(Double.toString(d1));   
       BigDecimal b2 = new BigDecimal(Double.toString(d2)); 
       BigDecimal b3 = new BigDecimal(Double.toString(d3));
       BigDecimal b4 = new BigDecimal(Double.toString(d4));
       double monthRateInvestD= format2decimal(b1.multiply(b2.divide(new BigDecimal(100)).divide(b3,6,BigDecimal.ROUND_HALF_UP)).multiply(b4));
       return monthRateInvestD;
   }
   
  
	
}
