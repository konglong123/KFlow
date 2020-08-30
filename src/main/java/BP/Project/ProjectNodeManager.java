package BP.Project;

import BP.springCloud.dao.ProjectNodeDao;
import BP.springCloud.entity.ProjectNode;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 项目节点管理器实现
 * @author pcc
 */
@Component
public class ProjectNodeManager{

	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(ProjectNodeManager.class);
	/**
	 * the ProjectNodeDao
	 */
	@Resource
	private ProjectNodeDao projectNodeDao;
	
	
	
	/*===============================================================================*/
	/*                                以下是增删改查方法
	/*===============================================================================*/
	/**
	 * 获取项目节点
	 * @param No
	 * @return the ProjectNode
	 */
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public ProjectNode getProjectNode(int No) {
		ProjectNode projectNode = projectNodeDao.getProjectNode(No);
		return projectNode;
	}
	/**
	 * 插入项目节点
	 * @param projectNode
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int insertProjectNode(ProjectNode projectNode) {
	     Date date = new Date();
		projectNodeDao.insertProjectNode(projectNode);
		int id=projectNode.getNo();
		return  id;
	}
	/**
	 * 更新项目节点
	 * @param projectNode
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Long updateProjectNode(ProjectNode projectNode) {
		return projectNodeDao.updateProjectNode(projectNode);
	}
	/**
	 * 删除项目节点
	 * @param No
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Long deleteProjectNode(int No) {
		return projectNodeDao.deleteProjectNode(No);
	}
	public List<ProjectNode> findProjectNodeList( ProjectNode projectNode) {
		return projectNodeDao.findProjectNodeList(projectNode);
	}

	/*===============================================================================*/
	/*                                以下是get/set方法
	/*===============================================================================*/
	/**
	 * @return the projectNodeDao
	 */
	public ProjectNodeDao getProjectNodeDao() {
		return this.projectNodeDao;
	}
	
	/**
	 * @param projectNodeDao the projectNodeDao to set
	 */
	public void setProjectNodeDao(ProjectNodeDao projectNodeDao) {
		this.projectNodeDao = projectNodeDao;
	}
	
}
