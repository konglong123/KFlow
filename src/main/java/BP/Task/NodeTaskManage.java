package BP.Task;

import BP.Sys.EnCfg;
import BP.springCloud.dao.NodeTaskMDao;
import BP.springCloud.entity.NodeTaskM;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-03-16 17:51
 **/
@Component
public class NodeTaskManage {
    private static final Logger log = Logger.getLogger(NodeTaskManage.class);
    @Resource
    private NodeTaskMDao nodeTaskMDao;

    public NodeTaskM getNodeTaskById(Long no){
        return nodeTaskMDao.getNodeTaskById(no);
    }

    //查询条件中yn有效,也就是默认查询是yn=0的数据，
    public List<NodeTaskM>  findNodeTaskList(NodeTaskM nodeTaskM){
        List<NodeTaskM> list=nodeTaskMDao.findNodeTaskList(nodeTaskM);
        if (list==null)
            return new ArrayList<NodeTaskM>();
        return list;
    }

    //查询条件中yn无效
    public List<NodeTaskM> findNodeTaskAllList(NodeTaskM nodeTaskM){
        nodeTaskM.setYn(2);
        List<NodeTaskM> list= nodeTaskMDao.findNodeTaskList(nodeTaskM);
        if (list==null)
            return new ArrayList<NodeTaskM>();
        return list;
    }

    public Long updateNodeTask(NodeTaskM nodeTask){

        return nodeTaskMDao.updateNodeTask(nodeTask);
    }

    public List getNodeTaskByNodeIdsAndParentTaskId(String parentTaskId,List<String> nodeIds){
        List list=nodeTaskMDao.getNodeTaskByNodeIdsAndParentTaskId(parentTaskId,nodeIds);
        if (list==null)
            return new ArrayList();
        return list;
    }

    public Long insertNodeTask(NodeTaskM nodeTaskM){

        return nodeTaskMDao.insertNodeTask(nodeTaskM);
    }


}
