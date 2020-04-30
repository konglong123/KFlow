package BP.Judge;

import BP.springCloud.dao.NodeRuleDao;
import BP.springCloud.entity.NodeRule;
import BP.springCloud.tool.FeignTool;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 节点规则管理器实现
 * @author pcc
 */
@Component
public class NodeRuleManager {

	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(NodeRuleManager.class);
	/**
	 * the NodeRuleDao
	 */
	@Resource
	private NodeRuleDao nodeRuleDao;
	
	
	
	/*===============================================================================*/
	/*                                以下是增删改查方法
	/*===============================================================================*/
	/**
	 * 获取节点规则
	 * @param No
	 * @return the NodeRule
	 */
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public NodeRule getNodeRule(int No) {
		NodeRule nodeRule = nodeRuleDao.getNodeRule(No);
		return nodeRule;
	}
	/**
	 * 插入节点规则
	 * @param nodeRule
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int insertNodeRule(NodeRule nodeRule) {
		Long id= FeignTool.getSerialNumber("BP.springCloud.entity.NodeRule");
		int no=Integer.parseInt(id+"");
		nodeRule.setNo(no);
		nodeRuleDao.insertNodeRule(nodeRule);
		return  no;
	}
	/**
	 * 更新节点规则
	 * @param nodeRule
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int updateNodeRule(NodeRule nodeRule) {
		return nodeRuleDao.updateNodeRule(nodeRule);
	}
	/**
	 * 删除节点规则
	 * @param No
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int deleteNodeRule(int No) {
		return nodeRuleDao.deleteNodeRule(No);
	}
	public List<NodeRule> findNodeRuleList(NodeRule nodeRule) {
		return nodeRuleDao.findNodeRuleList(nodeRule);
	}

	/*===============================================================================*/
	/*                                以下是get/set方法
	/*===============================================================================*/
	/**
	 * @return the nodeRuleDao
	 */
	public NodeRuleDao getNodeRuleDao() {
		return this.nodeRuleDao;
	}
	
	/**
	 * @param nodeRuleDao the nodeRuleDao to set
	 */
	public void setNodeRuleDao(NodeRuleDao nodeRuleDao) {
		this.nodeRuleDao = nodeRuleDao;
	}
	
}
