package cn.itcast.server.impl;

import cn.itcast.server.Bank;

public class BankImpl implements Bank {

	@Override
	public boolean recharge(String param) {
		System.out.println(param); //bankCardNum=1111111111111111111&money=1000.0
		// System.out.println("卡号:" + bankCardId + ",要转账:" + money + "元");

		//1.处理请求参数
		
		//2.根据银行卡号去表中查询
		
		//3.判断银行余额是否大于转账金额，进行转账
		
		
		return false;
	}

	
	
	
	@Override
	public void show() {
		System.out.println("bank show......");
	}

}
