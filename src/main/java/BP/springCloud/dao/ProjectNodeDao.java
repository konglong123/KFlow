package BP.springCloud.dao;

import BP.springCloud.entity.ProjectNode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目节点Dao
 * @author pcc
 * @Date 2020-08-26 12:46:11
 */
public interface ProjectNodeDao {

	/**
	 * 获取项目节点
	 * @param No
	 * @return the ProjectNode
	 */
	public ProjectNode getProjectNode(int No);

	/**
	 * 插入项目节点
	 * @param projectNode
	 */	 
	public Long insertProjectNode(ProjectNode projectNode);
	/**
	 * 更新项目节点
	 * @param projectNode
	 */
	public Long updateProjectNode(ProjectNode projectNode);
	/**
	 * 删除项目节点
	 * @param No
	 */
	public Long deleteProjectNode(int No);
	/**  
	* @Description: 按条件获取项目节点列表
	* @param
	* @param
	* @return 
	*/
	public List findProjectNodeList( @Param("po") ProjectNode projectNode);
}
