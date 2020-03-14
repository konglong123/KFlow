package BP.Task;

import BP.WF.Flow;
import BP.WF.Node;
import BP.springCloud.dao.NodeTaskMDao;
import BP.springCloud.entity.NodeTaskM;
import BP.springCloud.tool.KFlowTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-03-04 18:11
 **/
@Service
public class NodeTaskService {
    private  final Logger logger = LoggerFactory.getLogger(NodeTaskService.class);

    @Resource
    private NodeTaskMDao nodeTaskMDao;

    public List findNodeTaskList(NodeTaskM nodeTaskM){
        try {
            return nodeTaskMDao.findNodeTaskList(nodeTaskM);
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }

    public Long updateNodeTask(NodeTaskM nodeTaskM){
        return nodeTaskMDao.updateNodeTask(nodeTaskM);
    }

}
