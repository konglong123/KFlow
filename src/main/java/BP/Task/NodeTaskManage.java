package BP.Task;

import BP.springCloud.dao.NodeTaskMDao;
import BP.springCloud.entity.NodeTaskM;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-03-16 17:51
 **/
@Component
public class NodeTaskManage {
    @Resource
    private NodeTaskMDao nodeTaskMDao;

    public NodeTaskM getNodeTaskById(Long no){
        return nodeTaskMDao.getNodeTaskById(no);
    }

    public List findNodeTaskList(NodeTaskM nodeTaskM){
        return nodeTaskMDao.findNodeTaskList(nodeTaskM);
    }

    public List findNodeTaskAllList(NodeTaskM nodeTaskM){
        nodeTaskM.setYn(2);
        return nodeTaskMDao.findNodeTaskList(nodeTaskM);
    }

    public Long updateNodeTask(NodeTaskM nodeTask){
        return nodeTaskMDao.updateNodeTask(nodeTask);
    }

    public List getNodeTaskByNodeIds(String workId,List<String> nodeIds){
        return nodeTaskMDao.getNodeTaskByNodeIds(workId,nodeIds);
    }

    public Long insertNodeTask(NodeTaskM nodeTaskM){
        return nodeTaskMDao.insertNodeTask(nodeTaskM);
    }


}
