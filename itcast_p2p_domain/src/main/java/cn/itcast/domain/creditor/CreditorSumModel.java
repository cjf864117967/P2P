package cn.itcast.domain.creditor;

//债权信息表

public class CreditorSumModel {

	// 汇总信息
	private int dIdCount;// 合计记录
	private Double dDebtMoneySum;// 债权金额合计
	private Double dAvailableMoneySum;// 债权可用金额合计

	public int getdIdCount() {
		return dIdCount;
	}

	public void setdIdCount(int dIdCount) {
		this.dIdCount = dIdCount;
	}

	public Double getdDebtMoneySum() {
		return dDebtMoneySum;
	}

	public void setdDebtMoneySum(Double dDebtMoneySum) {
		this.dDebtMoneySum = dDebtMoneySum;
	}

	public Double getdAvailableMoneySum() {
		return dAvailableMoneySum;
	}

	public void setdAvailableMoneySum(Double dAvailableMoneySum) {
		this.dAvailableMoneySum = dAvailableMoneySum;
	}
}
