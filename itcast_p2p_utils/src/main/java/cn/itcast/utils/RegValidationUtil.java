package cn.itcast.utils;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

/**
  * 类的描述：
  * 版本：1.0
 */
public class RegValidationUtil {
//	public static final String USER_NAME="[\\u4E00-\\uFA29A-Za-z0-9_]{2,14}";
	public static final String REAL_NAME_EN="[A-Za-z]{6,24}";
	public static final String REAL_NAME_CH="[\\u4E00-\\uFA29]{2,8}";
//	public static final String PHONE_NO="^(1(([35][0-9])|(47)|[78][0123456789]))[0-9]{8}$";
	public static final String PHONE_NO="^1[0-9]{10}$";
	public static final String ID_CARD18="([0-9]{17}([0-9]|X))";
	public static final String ID_CARD15="([0-9]{15})";
	/**
	 * 日期验证 验证格式为yyyy-MM-dd
	 */
	public static final String DATE = "((^((1[8-9]\\d{2})|([2-9]\\d{3}))([-])(10|12|0?[13578])([-])(3[01]|[12][0-9]|0?[1-9])$)|(^((1[8-9]\\d{2})|([2-9]\\d{3}))([-])(11|0?[469])([-])(30|[12][0-9]|0?[1-9])$)|(^((1[8-9]\\d{2})|([2-9]\\d{3}))([-])(0?2)([-])(2[0-8]|1[0-9]|0?[1-9])$)|(^([2468][048]00)([-])(0?2)([-])(29)$)|(^([3579][26]00)([-])(0?2)([-])(29)$)|(^([1][89][0][48])([-])(0?2)([-])(29)$)|(^([2-9][0-9][0][48])([-])(0?2)([-])(29)$)|(^([1][89][2468][048])([-])(0?2)([-])(29)$)|(^([2-9][0-9][2468][048])([-])(0?2)([-])(29)$)|(^([1][89][13579][26])([-])(0?2)([-])(29)$)|(^([2-9][0-9][13579][26])([-])(0?2)([-])(29)$))";
	public static final String BANK_CARD="^[1-9][0-9]{14,22}";
	public static final String EMAIL="^[a-z]([a-z0-9]*[-_]?[a-z0-9]+)*@([a-z0-9]*[-_]?[a-z0-9]+)+[.][a-z]{2,3}([.][a-z]{2})?$";
	public static final String PASSWORD = "[A-Za-z0-9]{8,16}";
	public static final String IS_NUMBER="^([-]{0,1}[0-9]+){1}(([.]{1}[0-9]+)|([0-9]*))";
	public static final String IS_INTEGER="^([-]{0,1}[0-9]+){1}[0-9]{0,}";
	public static final String HAS_FORBIDDEN_MARK="[^`~!@#$%^&*+=|{}//[//]<>/?~！@#￥%……&*+|{}【】？]+";
	public static final String UNDERLINE="^[_]*";
	//一次编译所有正则表达式 
//	private static Pattern username =Pattern.compile(USER_NAME);
	private static Pattern realnameCH=Pattern.compile(REAL_NAME_CH);
	private static Pattern realnameEN=Pattern.compile(REAL_NAME_EN);
	private static Pattern phone=Pattern.compile(PHONE_NO);
	private static Pattern idCard15=Pattern.compile(ID_CARD15);
	private static Pattern idcard18=Pattern.compile(ID_CARD18);
	private static Pattern date=Pattern.compile(DATE);
	private static Pattern bankCard=Pattern.compile(BANK_CARD);
	private static Pattern email=Pattern.compile(EMAIL);
	private static Pattern password=Pattern.compile(PASSWORD);
	private static Pattern isNumber=Pattern.compile(IS_NUMBER);
	private static Pattern isInteger = Pattern.compile(IS_INTEGER);
	private static Pattern hasForbiddenMark = Pattern.compile(HAS_FORBIDDEN_MARK);
	private static Pattern underline=Pattern.compile(UNDERLINE);
	/**
	  * 方法的描述：验证字用户名
	  * @param input 输入的字符串
	  * @return boolean 返回：合符要求的返回真
	 */
//	public static boolean validateUsername(String input){
//		if(StringUtils.isEmpty(input)) return false;
//		return username.matcher(input).matches();
//	}
	/**
	  * 方法的描述：验证密码
	  * @param input 输入的字符串
	  * @return boolean 返回：合符要求的返回真
	 */
	public static boolean validatePassword(String input){
		if(StringUtils.isEmpty(input)) return false;
		return password.matcher(input).matches();
	}
	/**
	  * 方法的描述：验证英文名字
	  * @param input 输入的字符串
	  * @return boolean 返回：合符要求的返回真
	 */
	public static boolean validateRealName_EN(String input){
		if(StringUtils.isEmpty(input)) return false;
		return realnameEN.matcher(input).matches();
	}
	/**
	  * 方法的描述：验证中文名字
	  * @param input 输入的字符串
	  * @return boolean 返回：合符要求的返回真
	 */
	public static boolean validateRealName_CH(String input){
		if(StringUtils.isEmpty(input)) return false;
		return realnameCH.matcher(input).matches();
	}
	/**
	  * 方法的描述：验证手机号码
	  * @param input 输入的字符串
	  * @return boolean 返回：合符要求的返回真
	 */
	public static boolean validatePhoneNO(String input){
		if(StringUtils.isEmpty(input)) return false;
		return phone.matcher(input).matches();
	}
	/**
	  * 方法的描述：验证身份证
	  * @param input 输入的字符串
	  * @return boolean 返回：合符要求的返回真
	 */
	public static boolean validateIdCard(String input){
		if(StringUtils.isEmpty(input)) return false;
		return idcard18.matcher(input).matches()||idCard15.matcher(input).matches();
	}
	/**
	  * 方法的描述：验证日期 yyyy-MM-dd
	  * @param input 输入的字符串
	  * @return boolean 返回：合符格式要求的返回真
	 */
	public static boolean validateDate(String input){
		if(StringUtils.isEmpty(input)) return false;
		return date.matcher(input).matches();
	}
	/**
	  * 方法的描述：验证银行卡格式  （主要验证是不是数字构成 长度范围是否符合要求 ）
	  * @param input 输入的字符串
	  * @return boolean 返回：银行卡号正确时返回真
	 */
	public static boolean validateBankCard(String input){
		if(StringUtils.isEmpty(input)) return false;
		return bankCard.matcher(input).matches();
	}
	/**
	  * 方法的描述：验证邮箱地址
	  * @param input 输入的字符串
	  * @return boolean 返回：邮箱格式正确时 返回真
	 */
	public static boolean validateEmail(String input){
		if(StringUtils.isEmpty(input)) return false;
		return email.matcher(input).matches();
	}
	/**
	 * 方法描述：验证字符串长度 中文每个字长度算3 英文算1
	 * @param lowerLength 最小长度
	 * @param upperLength 最大长度
	 * @param input 验证字符串 按utf-8的方式验证
	 * @return boolean 返回是否验证通过
	 * @throws UnsupportedEncodingException 不支持的编码异常
	 */
	public static boolean validateStrLength(int lowerLength,int upperLength,String input) throws UnsupportedEncodingException{
		if(StringUtils.isEmpty(input)) return false;
		byte[] inputStr = input.getBytes("utf-8");
		if(inputStr.length<lowerLength) return false;
		if(inputStr.length>upperLength) return false;
		return true;
	}
	/**
	 * 方法描述：非空验证 支持数组 
	 * @param input 不定长数组
	 * @return boolean 全部为空时 返回true 否则返回假
	 */
	public static boolean validateAllInputIsEmpty(String...input ){
		if(null==input) return true;
		if(input.length==0) return true;
		for(int i=0;i<input.length;i++){
			if(null!=input[i]) return false;
			if(null==input[i]) continue;
			if(!"".equals(input[i].replaceAll(" ", ""))) return false;
		}
		return true;
	};
	/**
	  * 方法的描述：验证是否是数字
	  * @param input 输入的字符串
	  * @return boolean 返回：是数字时返回真
	 */
	public static boolean validateIsNumber(String input){
		if(StringUtils.isEmpty(input)) return false;
		return isNumber.matcher(input).matches();
	}
	
	/**
	  * 方法的描述：验证是否是数字
	  * @param input 输入的字符串
	  * @return boolean 返回：是数字时返回真
	 */
	public static boolean validateIsInteger(String input){
		if(StringUtils.isEmpty(input)) return false;
		return isInteger.matcher(input).matches();
	}
	/**
	  * 方法的描述：验证是否含有非法字符
	  * @param input 输入的字符串
	  * @return boolean 返回：含有时返回真 反之为假
	 */
	public static boolean validateHasForbiddenMark(String input){
		if(StringUtils.isEmpty(input)) return true;
		return !hasForbiddenMark.matcher(input).matches();
	}
	
	/**
	  * 方法的描述：验证是否全部为下划线(___)
	  * @param input 输入的字符串
	 */
	public static boolean validateUnderline(String input){	
		if(StringUtils.isEmpty(input)) return false;
		return !underline.matcher(input).matches();
	}

	
	public static void main(String[] args) throws UnsupportedEncodingException {
		
//		System.out.println(validateUnderline("_"));
		//System.out.println(validate("^[0-9]{2}[^0-9]?[b]+[a-g]{1,}", "22*bcc"));
		//System.out.println(validate(Regex.USER_NAME, "哈哈哈哈哈"));
		//System.out.println(validate(Regex.REAL_NAME_EN, "aas"));
		//System.out.println(validate(Regex.REAL_NAME_CH, "哈"));
		//System.out.println(validate(Regex.ID_CARD18, "50123412820228332X"));
		//System.out.println(validate(Regex.EMAIL, "a@b.cn"));
		//System.out.println(validate(Regex.DATE, "1922-2-22"));
		
//	   System.out.println(validateIsNumber("01111"));
//	   System.out.println(validateIsInteger("011"));
	//	byte[] strs = str.getBytes("utf-8");
//		byte[] strs3 = str.getBytes("gbk");
//		String str2 = new String(strs3, "gbk");
//		byte[] strs2 = str2.getBytes();
//		System.out.println(str2+"<<fa");
//		System.out.println(strs.length);
//		System.out.println("ss"+strs3.length);
	//	System.out.println(validateIdCard("330881860817831"));
		//boolean v = validateUsername("username");
		//System.out.println(v);
//		if(!v) System.out.println("butongguo");
		//System.out.println(validateAllInputIsEmpty("  "));
		//System.out.println(StringUtils.isEmpty(null));
		//System.out.println(validateIsNumber("2"));
		//System.out.println(validateHasForbiddenMark("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ4aW5oZTEyMyJ9.Wu37RZ0ZURJEqszCmFcyZzeL5d0U2F80_W82VYBQjsw北京"));
		//System.out.println(validateDate("2001-2-29"));
		//System.out.println(validateBankCard("22222222222222222"));
		//System.out.println(StringUtils.isEmpty("null"));
		//System.out.println(validateAllInputIsEmpty(null));
//		System.out.println(validatePhoneNO("10001131416"));
	//	System.out.println(validateIdCard("132930197104110"));
//		System.out.println(validateStrLength(4,16,"张"));
//		System.out.println(Locale.getDefault().getCountry());
//		System.out.println(TimeZone.getDefault().getID());
	}
	
	
}
