package BP.springCloud.dao;

import BP.springCloud.entity.Nlpmodel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * nlp训练模型Dao
 * @author pcc
 * @Date 2020-04-09 16:43:52
 */
public interface NlpmodelDao {

	/**
	 * 获取nlp训练模型
	 * @param id
	 * @return the Nlpmodel
	 */
	public Nlpmodel getNlpmodel(Long id);

	/**
	 * 插入nlp训练模型
	 * @param nlpmodel
	 */	 
	public int insertNlpmodel(Nlpmodel nlpmodel);
	/**
	 * 更新nlp训练模型
	 * @param nlpmodel
	 */
	public int updateNlpmodel(Nlpmodel nlpmodel);
	/**
	 * 删除nlp训练模型
	 * @param id
	 */
	public int deleteNlpmodel(int id);
	/**  
	* @Description: 按条件获取nlp训练模型列表
	* @param
	* @param
	* @return 
	*/
	public List findNlpmodelList(Nlpmodel nlpmodel);

	public void insertNlpmodelBatch(@Param("list") List<Nlpmodel> list);
}
