package BP.Project;

import BP.springCloud.entity.ProjectNode;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 项目节点服务实现
 * @author pcc
 */
@Component("projectNodeService")
public class ProjectNodeService{
	/**
	 * Logger for this class
	 */
	 private static final Logger log = Logger.getLogger(ProjectNodeService.class);
	/**
	 * the ProjectNodeManager
	 */
	@Resource
	private ProjectNodeManager projectNodeManager;
	/* 
	 * (non-Javadoc)
	 * @see WF.Nlp.service.ProjectNodeService\#add(WF.Nlp.domain.ProjectNode)
	 */
	public int insertProjectNode(ProjectNode projectNode) {
		return projectNodeManager.insertProjectNode(projectNode);
	}
	/* 
	 * (non-Javadoc)
	 * @see WF.Nlp.service.ProjectNodeService\#update(WF.Nlp.domain.ProjectNode)
	 */
	public Long updateProjectNode(ProjectNode projectNode) {
		return projectNodeManager.updateProjectNode(projectNode);
	}
	/* 
	 * (non-Javadoc)
	 * @seeWF.Nlp.service.ProjectNodeService\#delete(java.lang.Long)
	 */
	public Long deleteProjectNode(int id) {
		return projectNodeManager.deleteProjectNode(id);
	}
	/* 
	 * (non-Javadoc)
	 * @see WF.Nlp.service.ProjectNodeService\#getProjectNode(Long id)
	 */
	public ProjectNode getProjectNode(int id) {
		return projectNodeManager.getProjectNode(id);
	}
	/* 
	 * (non-Javadoc)
	 * @see WF.Nlp.service.ProjectNodeService\#getProjectNodeListPage(${entityMeta.projectMeta.groupId}.domain.ProjectNode, ${entityMeta.projectMeta.groupId}.common.Page)
	 */
	public List<ProjectNode> findProjectNodeList(ProjectNode projectNodeCondition) {
		return projectNodeManager.findProjectNodeList( projectNodeCondition);
	}
}
