package BP.Nlp;

import BP.springCloud.dao.NlpmodelDao;
import BP.springCloud.entity.Nlpmodel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * nlp训练模型管理器实现
 * @author pcc
 */
@Component
public class NlpmodelManager {

	/**
	 * Logger for this class
	 */
	private static Logger logger = LoggerFactory.getLogger(NlpmodelManager.class);


	public List findNlpmodelList(Nlpmodel nlpmodel) {
		return nlpmodelDao.findNlpmodelList(nlpmodel);
	}

	/**
	 * the NlpmodelDao
	 */
	@Resource
	private NlpmodelDao nlpmodelDao;
	
	
	
	/*===============================================================================*/
	/*                                以下是增删改查方法
	/*===============================================================================*/
	/**
	 * 获取nlp训练模型
	 * @param id
	 * @return the Nlpmodel
	 */
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Nlpmodel getNlpmodel(int id) {
		Nlpmodel nlpmodel = nlpmodelDao.getNlpmodel(id);
		return nlpmodel;
	}
	/**
	 * 插入nlp训练模型
	 * @param nlpmodel
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int insertNlpmodel(Nlpmodel nlpmodel) {
	     Date date = new Date();
	     nlpmodel.setIsDelete(0);
		 nlpmodel.setCreateTime(date);
		nlpmodelDao.insertNlpmodel(nlpmodel);
		int id=nlpmodel.getNo();
		return  id;
	}
	/**
	 * 更新nlp训练模型
	 * @param nlpmodel
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int updateNlpmodel(Nlpmodel nlpmodel) {
		nlpmodel.setUpdateTime(new Date());
		return nlpmodelDao.updateNlpmodel(nlpmodel);
	}
	/**
	 * 删除nlp训练模型
	 * @param id
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int deleteNlpmodel(int id) {
		return nlpmodelDao.deleteNlpmodel(id);
	}

	/*===============================================================================*/
	/*                                以下是get/set方法
	/*===============================================================================*/
	/**
	 * @return the nlpmodelDao
	 */
	public NlpmodelDao getNlpmodelDao() {
		return this.nlpmodelDao;
	}
	
	/**
	 * @param nlpmodelDao the nlpmodelDao to set
	 */
	public void setNlpmodelDao(NlpmodelDao nlpmodelDao) {
		this.nlpmodelDao = nlpmodelDao;
	}
	
}
