package BP.springCloud.dao;

import BP.springCloud.entity.GenerFlow;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 流程实例Dao
 * @author pcc
 * @Date 2020-03-17 13:06:32
 */
public interface GenerFlowDao {

	/**
	 * 获取流程实例
	 * @param no
	 * @return the GenerFlow
	 */
	public GenerFlow getGenerFlowById(Long no);

	/**
	 * 插入流程实例
	 * @param kGenerFlow
	 */
	public Long insertGenerFlow(GenerFlow kGenerFlow);
	/**
	 * 更新流程实例
	 * @param kGenerFlow
	 */
	public Long updateGenerFlow(GenerFlow kGenerFlow);
	/**
	 * 删除流程实例
	 * @param no
	 */
	public Long deleteGenerFlow(Long no);

	public List findGenerFlowList( @Param("po") GenerFlow generFlow);
}
