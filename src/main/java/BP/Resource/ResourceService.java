package BP.Resource;

import BP.Task.NodeTask;
import BP.Task.NodeTaskAttr;
import BP.springCloud.dao.ResourceTaskMDao;
import BP.springCloud.entity.NodeTaskM;
import BP.springCloud.entity.ResourceTaskM;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-02-28 10:07
 **/
@Service
public class ResourceService {
    private final Logger logger = LoggerFactory.getLogger(ResourceService.class);

    @Resource
    private ResourceTaskMDao resourceTaskMDao;

    public JSONObject getResourceLoad(String resourceNo){
        return null;
    }

    public JSONObject getResourceKindGant(String resourceNo){

        try{

            ResourceTaskM resourceTaskM=new ResourceTaskM();
            resourceTaskM.setResourceNo(resourceNo);

            //计划完成(已经计划)
            resourceTaskM.setIsPlan(1);
            List<ResourceTaskM> planList=resourceTaskMDao.findResourceTaskList(resourceTaskM);

            ResourceItems items=new ResourceItems();
            items.Retrieve(ResourceItemAttr.Kind,resourceNo);
            List<ResourceItem> itemList=items.toList();
            Map<String,List<JSONObject>> map=new HashMap<>();

            for (ResourceItem item:itemList){
                List<JSONObject> data=new ArrayList<>();
                map.put(item.getNo(),data);
            }

            int shiCha=8 * 60 * 60 * 1000;
            for (ResourceTaskM taskM:planList){
                String resourceId=taskM.getResourceId();
                if (StringUtils.isEmpty(resourceId))
                    continue;
                List<JSONObject> listTemp=map.get(resourceId);
                JSONObject temp=new JSONObject();
                temp.put("id",taskM.getNo());
                temp.put("start",taskM.getPlanStart().getTime()+shiCha);
                temp.put("end",taskM.getPlanEnd().getTime()+shiCha);
                temp.put("resId",resourceId);
                listTemp.add(temp);
            }

            JSONObject result=new JSONObject();
            List<JSONObject> data=new ArrayList<>();
            for (Map.Entry<String,List<JSONObject>> entry:map.entrySet()){
                JSONObject temp=new JSONObject();
                temp.put("model",entry.getKey());
                temp.put("current",0);
                temp.put("deals",entry.getValue());
                data.add(temp);
            }
            result.put("series",data);
            return result;

        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }

    public JSONObject getResourceItemGant(String resourceItemNo){
        try{

            ResourceTaskM resourceTaskM=new ResourceTaskM();
            resourceTaskM.setResourceId(resourceItemNo);

            //计划完成(已经计划)
            resourceTaskM.setIsPlan(1);
            List<ResourceTaskM> planList=resourceTaskMDao.findResourceTaskList(resourceTaskM);

            Map<String,List<JSONObject>> map=new HashMap<>();

            int shiCha=8 * 60 * 60 * 1000;
            for (ResourceTaskM taskM:planList){
                String taskId=taskM.getTaskId();
                if (StringUtils.isEmpty(taskId))
                    continue;
                NodeTask task=new NodeTask(taskId);
                String projectNo=task.GetValStrByKey(NodeTaskAttr.WorkGroupId);
                List<JSONObject> listTemp=map.get(projectNo);
                if (listTemp==null) {
                    listTemp = new ArrayList<>();
                    map.put(projectNo,listTemp);
                }
                JSONObject temp=new JSONObject();
                temp.put("id",taskM.getNo());
                temp.put("start",taskM.getPlanStart().getTime()+shiCha);
                temp.put("end",taskM.getPlanEnd().getTime()+shiCha);
                temp.put("nTaskId",taskM.getTaskId());
                temp.put("projectId",projectNo);
                listTemp.add(temp);
            }

            JSONObject result=new JSONObject();
            List<JSONObject> data=new ArrayList<>();
            for (Map.Entry<String,List<JSONObject>> entry:map.entrySet()){
                JSONObject temp=new JSONObject();
                temp.put("model",entry.getKey());
                temp.put("current",0);
                temp.put("deals",entry.getValue());
                data.add(temp);
            }
            result.put("series",data);
            return result;

        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
    *@Description: 新增资源任务 (预定资源)
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/2/29
    */
    public void bookResource(ResourceTaskM resourceTaskM){

        /*resourceTaskM.setWorkId(0L);//默认时，表示流程未发起*/

        //没有计划时，计划时间为预定时间，没有完成时，完成时间为计划时间）
        resourceTaskM.setPlanStart(resourceTaskM.getBookStart());
        resourceTaskM.setPlanEnd(resourceTaskM.getBookEnd());
        resourceTaskM.setEndTime(resourceTaskM.getBookEnd());
        resourceTaskM.setStartTime(resourceTaskM.getBookStart());

        //新增资源任务，默认is_plan=2(未计划)，is_finish=2（未完成）
        resourceTaskM.setIsPlan(2);
        resourceTaskM.setIsFinish(2);

        resourceTaskMDao.addResourceTask(resourceTaskM);
    }

    public void updateResourceTask(ResourceTaskM resourceTaskM){
        if (resourceTaskM.getNo()!=null)
            resourceTaskMDao.updateResourceTask(resourceTaskM);
    }

    public void deleteResourceTask(String no){
        resourceTaskMDao.deleteResourceTask(no);
    }


}
