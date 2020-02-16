package cn.itcast.domain.matchManagement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 类描述：权重规则设置表  实体
 */
@Entity
@Table(name="T_WEIGHRULE")
public class WeigthRule {
	
	@Id
	@GeneratedValue()
	@Column(name="T_ID", nullable=false)
	private Integer id;//主键
	
	@Column(name="T_SERIALNO", nullable=false)
	private String SerialNo;//类别编号
	
	@Column(name="T_WEIGHRULE_NAME", nullable=false)
	private String weigthRuleName;//权重规则类型名称
	
	@Column(name="T_WEIGTHRULE_CLASS", nullable=false)
	private Integer weigthRuleClass;//权重类别  资金类 140 债权类 140
	
	@Column(name="T_WEIGTH_TYPE", nullable=false)
	private Integer weigthType;//权重类型   
	
	@Column(name="T_WEIGTH_VALUE", nullable=false)
	private Integer weigthValue;//权重值
	
	@Column(name="T_ORDER", nullable=false)
	private int order; //排序
	
	@Column(name="T_WEIGTHRULE_CLASSNAME", nullable=false)
	private String weigthRuleClassName;// 权重类别名称
	
	
	
	/**
	 * @return id
	 *
	 */
	
	public Integer getId() {
		return id;
	}



	/**
	 * @param id 要设置的 id
	 *
	 */
	public void setId(Integer id) {
		this.id = id;
	}



	/**
	 * @return serialNo
	 *
	 */
	
	public String getSerialNo() {
		return SerialNo;
	}



	/**
	 * @param serialNo 要设置的 serialNo
	 *
	 */
	public void setSerialNo(String serialNo) {
		SerialNo = serialNo;
	}



	/**
	 * @return weigthRuleName
	 *
	 */
	
	public String getWeigthRuleName() {
		return weigthRuleName;
	}



	/**
	 * @param weigthRuleName 要设置的 weigthRuleName
	 *
	 */
	public void setWeigthRuleName(String weigthRuleName) {
		this.weigthRuleName = weigthRuleName;
	}



	/**
	 * @return weigthRuleClass
	 *
	 */
	
	public Integer getWeigthRuleClass() {
		return weigthRuleClass;
	}



	/**
	 * @param weigthRuleClass 要设置的 weigthRuleClass
	 *
	 */
	public void setWeigthRuleClass(Integer weigthRuleClass) {
		this.weigthRuleClass = weigthRuleClass;
	}



	/**
	 * @return weigthType
	 *
	 */
	
	public Integer getWeigthType() {
		return weigthType;
	}



	/**
	 * @param weigthType 要设置的 weigthType
	 *
	 */
	public void setWeigthType(Integer weigthType) {
		this.weigthType = weigthType;
	}



	/**
	 * @return weigthValue
	 *
	 */
	
	public Integer getWeigthValue() {
		return weigthValue;
	}



	/**
	 * @param weigthValue 要设置的 weigthValue
	 *
	 */
	public void setWeigthValue(Integer weigthValue) {
		this.weigthValue = weigthValue;
	}



	/**
	 * @return order
	 *
	 */
	
	public int getOrder() {
		return order;
	}



	/**
	 * @param order 要设置的 order
	 *
	 */
	public void setOrder(int order) {
		this.order = order;
	}



	/**
	 * @return weigthRuleClassName
	 *
	 */
	
	public String getWeigthRuleClassName() {
		return weigthRuleClassName;
	}



	/**
	 * @param weigthRuleClassName 要设置的 weigthRuleClassName
	 *
	 */
	public void setWeigthRuleClassName(String weigthRuleClassName) {
		this.weigthRuleClassName = weigthRuleClassName;
	}



	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
	}
	
	
}
