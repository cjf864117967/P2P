package cn.itcast.domain.productAccount;

import java.io.Serializable;
import java.util.Date;

public class WaitMatchMoneyModel implements Serializable {
	
	private static final long serialVersionUID = 227L;
	private Integer id;
	private Integer recordedId; // 记录id ,已匹配
	private Integer investRecordId; // 投资记录ID,待匹配
	private Integer userId; // 用户id
	private Integer mainaccountId; // 主账户id
	private Integer subAccountId; // 投资编号（t_account_log,子账号id）
	private Integer fundWeight; // 资金权重
	private String userName; // 用户名
	private String pSerialNo; // 投资编号(t_product_account购买产品记录的投资编号，相当于记录主键)
	private Double amountWait; // 待匹配金额
	private String amountWaitDes; // 待匹配金额描述
	private Double amountAll; // 投资金额（包含已匹配和待匹配）
	private String amountAllDes; // 投资金额描述
	private Double amountMatch; // 匹配金额（每次匹配的金额）
	private String amountMatchDes; // 匹配金额描述
	private String productName; // 产品名
	private Date date; // 投资时间
	private String dateDescrible; // 时间描述
	private Date confirmDate; // 确认时间
	private Integer investType; // 投资类型
	private String investTypeDescrible; // 投资类型描述
	private Integer deadline; // 投资期限
	private int currentMonth; // 当前期
	private Integer count; // 列表大小
	private Double sum; // 资金总和
	private Integer status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRecordedId() {
		return recordedId;
	}

	public void setRecordedId(Integer recordedId) {
		this.recordedId = recordedId;
	}

	public Integer getInvestRecordId() {
		return investRecordId;
	}

	public void setInvestRecordId(Integer investRecordId) {
		this.investRecordId = investRecordId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getMainaccountId() {
		return mainaccountId;
	}

	public void setMainaccountId(Integer mainaccountId) {
		this.mainaccountId = mainaccountId;
	}

	public Integer getSubAccountId() {
		return subAccountId;
	}

	public void setSubAccountId(Integer subAccountId) {
		this.subAccountId = subAccountId;
	}

	public Integer getFundWeight() {
		return fundWeight;
	}

	public void setFundWeight(Integer fundWeight) {
		this.fundWeight = fundWeight;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getpSerialNo() {
		return pSerialNo;
	}

	public void setpSerialNo(String pSerialNo) {
		this.pSerialNo = pSerialNo;
	}

	public Double getAmountWait() {
		return amountWait;
	}

	public void setAmountWait(Double amountWait) {
		this.amountWait = amountWait;
	}

	public String getAmountWaitDes() {
		return amountWaitDes;
	}

	public void setAmountWaitDes(String amountWaitDes) {
		this.amountWaitDes = amountWaitDes;
	}

	public Double getAmountAll() {
		return amountAll;
	}

	public void setAmountAll(Double amountAll) {
		this.amountAll = amountAll;
	}

	public String getAmountAllDes() {
		return amountAllDes;
	}

	public void setAmountAllDes(String amountAllDes) {
		this.amountAllDes = amountAllDes;
	}

	public Double getAmountMatch() {
		return amountMatch;
	}

	public void setAmountMatch(Double amountMatch) {
		this.amountMatch = amountMatch;
	}

	public String getAmountMatchDes() {
		return amountMatchDes;
	}

	public void setAmountMatchDes(String amountMatchDes) {
		this.amountMatchDes = amountMatchDes;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDateDescrible() {
		return dateDescrible;
	}

	public void setDateDescrible(String dateDescrible) {
		this.dateDescrible = dateDescrible;
	}

	public Date getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}

	public Integer getInvestType() {
		return investType;
	}

	public void setInvestType(Integer investType) {
		this.investType = investType;
	}

	public String getInvestTypeDescrible() {
		return investTypeDescrible;
	}

	public void setInvestTypeDescrible(String investTypeDescrible) {
		this.investTypeDescrible = investTypeDescrible;
	}

	public Integer getDeadline() {
		return deadline;
	}

	public void setDeadline(Integer deadline) {
		this.deadline = deadline;
	}

	public int getCurrentMonth() {
		return currentMonth;
	}

	public void setCurrentMonth(int currentMonth) {
		this.currentMonth = currentMonth;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Double getSum() {
		return sum;
	}

	public void setSum(Double sum) {
		this.sum = sum;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
