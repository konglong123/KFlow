package BP.Nlp;
import BP.springCloud.entity.Nlpmodel;
import BP.springCloud.tool.FeignTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * nlp训练模型服务实现
 * @author pcc
 */
@Service("nlpmodelService")
public class NlpmodelService {
	/**
	 * Logger for this class
	 */
	private static Logger logger = LoggerFactory.getLogger(NlpmodelService.class);
	/**
	 * the NlpmodelManager
	 */
	@Resource
	private NlpmodelManager nlpmodelManager;


	/* 
	 * (non-Javadoc)
	 * @see WF.Nlp.service.NlpmodelService\#add(WF.Nlp.domain.Nlpmodel)
	 */
	public Long insertNlpmodel(Nlpmodel nlpmodel) {
		nlpmodel.setId(FeignTool.getSerialNumber("BP.springCloud.entity.Nlpmodel"));
		return nlpmodelManager.insertNlpmodel(nlpmodel);
	}
	/* 
	 * (non-Javadoc)
	 * @see WF.Nlp.service.NlpmodelService\#update(WF.Nlp.domain.Nlpmodel)
	 */
	public int updateNlpmodel(Nlpmodel nlpmodel) {
		return nlpmodelManager.updateNlpmodel(nlpmodel);
	}
	/* 
	 * (non-Javadoc)
	 * @seeWF.Nlp.service.NlpmodelService\#delete(java.lang.Long)
	 */
	public int deleteNlpmodel(int id) {
		return nlpmodelManager.deleteNlpmodel(id);
	}
	/* 
	 * (non-Javadoc)
	 * @see WF.Nlp.service.NlpmodelService\#getNlpmodel(Long id)
	 */
	public Nlpmodel getNlpmodel(Long id) {
		return nlpmodelManager.getNlpmodel(id);
	}
	/* 
	 * (non-Javadoc)
	 * @see WF.Nlp.service.NlpmodelService\#getNlpmodelListPage(${entityMeta.projectMeta.groupId}.domain.Nlpmodel, ${entityMeta.projectMeta.groupId}.common.Page)
	 */
	public List findNlpmodelList(Nlpmodel nlpmodel) {
		List list= nlpmodelManager.findNlpmodelList(nlpmodel);
		if (list==null)
			list=new ArrayList(1);
		return list;
	}
}
