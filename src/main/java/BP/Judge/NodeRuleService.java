package BP.Judge;

import BP.springCloud.entity.NodeRule;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 节点规则服务实现
 * @author pcc
 */
@Component("nodeRuleService")
public class NodeRuleService {
	/**
	 * Logger for this class
	 */
	 private static final Logger log = Logger.getLogger(NodeRuleService.class);
	/**
	 * the NodeRuleManager
	 */
	@Resource
	private NodeRuleManager nodeRuleManager;
	/* 
	 * (non-Javadoc)
	 * @see WF.Nlp.service.NodeRuleService\#add(WF.Nlp.domain.NodeRule)
	 */
	public int insertNodeRule(NodeRule nodeRule) {
		return nodeRuleManager.insertNodeRule(nodeRule);
	}
	/* 
	 * (non-Javadoc)
	 * @see WF.Nlp.service.NodeRuleService\#update(WF.Nlp.domain.NodeRule)
	 */
	public int updateNodeRule(NodeRule nodeRule) {
		return nodeRuleManager.updateNodeRule(nodeRule);
	}
	/* 
	 * (non-Javadoc)
	 * @seeWF.Nlp.service.NodeRuleService\#delete(java.lang.Long)
	 */
	public int deleteNodeRule(int id) {
		return nodeRuleManager.deleteNodeRule(id);
	}
	/* 
	 * (non-Javadoc)
	 * @see WF.Nlp.service.NodeRuleService\#getNodeRule(Long id)
	 */
	public NodeRule getNodeRule(int id) {
		return nodeRuleManager.getNodeRule(id);
	}
	/* 
	 * (non-Javadoc)
	 * @see WF.Nlp.service.NodeRuleService\#getNodeRuleListPage(${entityMeta.projectMeta.groupId}.domain.NodeRule, ${entityMeta.projectMeta.groupId}.common.Page)
	 */
	public List<NodeRule> findNodeRuleList(NodeRule nodeRuleCondition) {
		return nodeRuleManager.findNodeRuleList(nodeRuleCondition);
	}
}
