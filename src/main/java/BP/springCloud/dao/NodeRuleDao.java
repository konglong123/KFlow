package BP.springCloud.dao;

import BP.springCloud.entity.NodeRule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 节点规则Dao
 * @author pcc
 * @Date 2020-04-29 14:37:44
 */
public interface NodeRuleDao {

	/**
	 * 获取节点规则
	 * @param No
	 * @return the NodeRule
	 */
	public NodeRule getNodeRule(int No);

	/**
	 * 插入节点规则
	 * @param nodeRule
	 */	 
	public int insertNodeRule(NodeRule nodeRule);
	/**
	 * 更新节点规则
	 * @param nodeRule
	 */
	public int updateNodeRule(NodeRule nodeRule);
	/**
	 * 删除节点规则
	 * @param No
	 */
	public int deleteNodeRule(int No);
	/**  
	* @Description: 按条件获取节点规则列表
	* @param
	* @param
	* @return 
	*/
	public List findNodeRuleList(NodeRule nodeRule);
}
