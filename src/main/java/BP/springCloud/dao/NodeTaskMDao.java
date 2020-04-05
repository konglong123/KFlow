package BP.springCloud.dao;

import BP.springCloud.entity.NodeTaskM;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 节点任务Dao
 * @author pcc
 * @Date 2020-03-09 18:12:02
 */
public interface NodeTaskMDao {

	/**
	 * 获取节点任务
	 * @param no
	 * @return the NodeTask
	 */
	public NodeTaskM getNodeTaskById(Long no);

	/**
	 * 插入节点任务
	 * @param nodeTask
	 */	 
	public Long insertNodeTask(NodeTaskM nodeTask);
	/**
	 * 更新节点任务
	 * @param nodeTask
	 */
	public Long updateNodeTask(NodeTaskM nodeTask);
	/**
	 * 删除节点任务
	 * @param no
	 */
	public Long deleteNodeTask(Long no);
	/**  
	* @Description: 按条件获取节点任务列表
	* @param
	* @param
	* @return 
	*/
	public List findNodeTaskList(NodeTaskM nodeTaskM);

	public List getNodeTaskByNodeIdsAndParentTaskId(@Param("parentTaskId") String parentTaskId,@Param("ids") List<String> nodeIds);
}
