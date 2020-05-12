package BP.WF;

/** 
 节点工作退回规则
 
*/
public enum ReturnRole
{
	/** 
	 不能退回
	 
	*/
	CanNotReturn,

	/** 
	 可退回指定的节点
	 
	*/
	ReturnSpecifiedNodes;

	public int getValue()
	{
		return this.ordinal();
	}

	public static ReturnRole forValue(int value)
	{
		return values()[value];
	}
}