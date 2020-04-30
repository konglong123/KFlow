package BP.springCloud.dao;

import BP.springCloud.entity.JudgeRoute;

import java.util.List;

/**
 * 决策路由Dao
 * @author pcc
 * @Date 2020-04-30 21:06:15
 */
public interface JudgeRouteDao {

	/**
	 * 获取决策路由
	 * @param No
	 * @return the JudgeRoute
	 */
	public JudgeRoute getJudgeRoute(Long No);

	/**
	 * 插入决策路由
	 * @param judgeRoute
	 */	 
	public Long insertJudgeRoute(JudgeRoute judgeRoute);
	/**
	 * 更新决策路由
	 * @param judgeRoute
	 */
	public Long updateJudgeRoute(JudgeRoute judgeRoute);
	/**
	 * 删除决策路由
	 * @param No
	 */
	public Long deleteJudgeRoute(Long No);
	/**  
	* @Description: 按条件获取决策路由列表
	* @param
	* @param
	* @return 
	*/
	public List<JudgeRoute> findJudgeRouteList(JudgeRoute judgeRoute);
}
