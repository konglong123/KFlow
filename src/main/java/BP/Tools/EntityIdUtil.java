package BP.Tools;

import BP.WF.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: kflow-web
 * @description: entity类生成Id编码
 * @author: Mr.Kong
 * @create: 2019-12-20 11:27
 **/
public class EntityIdUtil {

    /**
    *@Description: 生成Flow的新节点Id， 
    *@Param:  
    *@return:  
    *@Author: Mr.Wang 
    *@Date: 2019/12/20 
    */
    public static List<String> getNodeIds(List<Node> nodesCurrent,String flowNo, int num){
        int idx=1;
        Map<Integer,Node> nodeMap=new HashMap<>();
        for (Node node:nodesCurrent){
            nodeMap.put(node.getNodeID(),node);
        }
        int count=0;
        List<String> nodeIds=new ArrayList<>(num);
        while (true) {
            //凑齐两位，一位时左补0，（限制一个流程中最多有99个节点）
            String strID = flowNo + StringHelper.padLeft(String.valueOf(idx), 2, '0');
            if (!nodeMap.containsKey(Integer.parseInt(strID))){
                nodeIds.add(strID);
                count++;
            }
            idx++;
            if (count==num){
                break;
            }
        }
        return nodeIds;
    }

}
