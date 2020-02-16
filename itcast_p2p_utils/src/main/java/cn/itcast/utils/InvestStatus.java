package cn.itcast.utils;
/**
 * 类描述：产品投资-投资状态
 */
public interface InvestStatus {
	/**待匹配	10901**/
	public static final int WAIT_TO_MATCH = 10901;
	/**待确认	10902 confirm**/
	public static final int WAIT_CONFIRM = 10902;
	/**回收中	10903 recycle**/
	public static final int IN_RECYCLING = 10903;
	/**已回收	10904**/
	public static final int ALREADY_RECYCLED = 10904;
	/**匹配中	10905**/
	public static final int IN_MATCHING = 10905;
	/**已撤销	10906  Revocation**/
	public static final int ALREADY_REVOCATION = 10906;
	/**提前赎回 10907  Early Redemption**/
	public static final int EARLY_REDEMPTION = 10907;
}
