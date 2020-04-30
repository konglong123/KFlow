package BP.springCloud.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 决策规则
 * @author pcc
 */
public class JudgeRuleM implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	private int No;
	
	/**
	 * 别名
	 */
	private String alias;
	
	/**
	 * 类型（1表达式，2Bean）
	 */
	private int type;
	
	/**
	 * 表达式内容
	 */
	private String expression;
	
	/**
	 * beanId（spring中id）
	 */
	private String beanId;
	
	/**
	 * 备注
	 */
	private String context;
	
	/**
	 * 创建者
	 */
	private String creator;
	
	/**
	 * 创建时间
	 */
	private Date creatTime;
	
	/**
	 * 更新时间
	 */
	private Date updateTime;
	
	/**
	 * 是否经过检测
	 */
	private int isTest;
	
	/**
	 * @return the No
	 */
	public int getNo() {
		return No;
	}
	
	/**
	 * @param No the No to set
	 */
	public void setNo(int No) {
		this.No = No;
	}
	
	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}
	
	/**
	 * @param alias the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	/**
	 * @return the expression
	 */
	public String getExpression() {
		return expression;
	}
	
	/**
	 * @param expression the expression to set
	 */
	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	/**
	 * @return the beanId
	 */
	public String getBeanId() {
		return beanId;
	}
	
	/**
	 * @param beanId the beanId to set
	 */
	public void setBeanId(String beanId) {
		this.beanId = beanId;
	}
	
	/**
	 * @return the context
	 */
	public String getContext() {
		return context;
	}
	
	/**
	 * @param context the context to set
	 */
	public void setContext(String context) {
		this.context = context;
	}
	
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	/**
	 * @return the creatTime
	 */
	public Date getCreatTime() {
		return creatTime;
	}
	
	/**
	 * @param creatTime the creatTime to set
	 */
	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}
	
	/**
	 * @return the updateTime
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	
	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	/**
	 * @return the isTest
	 */
	public int getIsTest() {
		return isTest;
	}
	
	/**
	 * @param isTest the isTest to set
	 */
	public void setIsTest(int isTest) {
		this.isTest = isTest;
	}
	
}