package BP.Judge;

import BP.springCloud.entity.NodeTaskM;

/**
 * @program: kflow-web
 * @description:判断条件，所有决策节点中指定的决策bean必须实现该接口
 * @author: Mr.Kong
 * @create: 2020-04-25 11:03
 **/
public interface JudgeCondition {

    /**
    *@Description: 判断，返回决策的流向的节点Id
    *@Param:  taskM 当前节点任务
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/4/25
    */
    boolean judge(NodeTaskM taskM);
}
