package BP.Resource;

import BP.springCloud.dao.ResourceTaskMDao;
import BP.springCloud.entity.ResourceTaskM;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

    public JSONObject getResourceLoadForHighcharts(String resourceNo, String startTime, String endTime){

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date start = sdf.parse(startTime);
            Date end=sdf.parse(endTime);

            //检验输入
            if (start.getTime()>end.getTime()){
                String temp=startTime;
                startTime=endTime;
                endTime=temp;
            }
            ResourceTaskM resourceTaskM=new ResourceTaskM();
            resourceTaskM.setResourceNo(resourceNo);

            //计划完成(已经计划，但是没有完成的任务)
            resourceTaskM.setIsFinish(2);
            resourceTaskM.setIsPlan(1);
            List<ResourceTaskM> planList=resourceTaskMDao.getResourceTaskByPlanTime(resourceTaskM,startTime,endTime);

            //计划中（已经预定，但是没有统一计划的任务）
            resourceTaskM.setIsFinish(2);
            resourceTaskM.setIsPlan(2);
            List<ResourceTaskM> bookList=resourceTaskMDao.getResourceTaskByPlanTime(resourceTaskM,startTime,endTime);

            //已预定（没有完成，预定加计划完成任务）
            resourceTaskM.setIsPlan(0);
            resourceTaskM.setIsFinish(2);
            List<ResourceTaskM> bookPlanList=resourceTaskMDao.getResourceTaskByPlanTime(resourceTaskM,startTime,endTime);

            //推荐预定（没有被计划、没有被预定对时间段）
            List<ResourceTaskM> RecommendList =new ArrayList();

            JSONObject jsonObject=new JSONObject();
            jsonObject.put("finishPlan",transToHighcharts(planList,start,end));
            jsonObject.put("book",transToHighcharts(bookList,start,end));
            jsonObject.put("bookPlan",transToHighcharts(bookPlanList,start,end));
            jsonObject.put("categories",getCategories(start,end));
            return jsonObject;

        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }

    private List getCategories(Date start,Date end){
        Calendar calendar=Calendar.getInstance();
        int rat=1000*60*60*24;
        int dayNum=(int)((end.getTime()-start.getTime())/rat)+1;
        calendar.setTime(start);

        List<String> categories=new ArrayList<>();
        StringBuilder sb=new StringBuilder();
        do {
            int y=calendar.get(Calendar.YEAR);
            int m=calendar.get(Calendar.MONTH)+1;
            int d=calendar.get(Calendar.DATE);
            categories.add(y+"/"+m+"/"+d);
            calendar.add(Calendar.DATE,1);
            dayNum--;
        }while (dayNum>0);
        return categories;
    }
    
    /**
    *@Description: 将list转换成highcharts中data的json格式
    *@Param:  
    *@return:  
    *@Author: Mr.kong
    *@Date: 2020/2/28 
    */
    private String transToHighcharts(List<ResourceTaskM> list,Date start,Date end) throws Exception{
        StringBuilder sb=new StringBuilder();
        sb.append("[");
        if (list!=null) {
            int ratDay=1000*60*60*24;
            int ratHour=1000*60*60;
            int dayNum=(int)((end.getTime()-start.getTime())/ratDay)+1;
            List<List> finishPlan=new ArrayList<>(dayNum);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date base = sdf.parse("2000-01-01");

            for (ResourceTaskM temp : list) {
                Date planStart=temp.getPlanStart();
                Date planEnd=temp.getPlanEnd();
                int pos=(int)((planStart.getTime()-start.getTime())/ratDay);
                int planStartHour=(int)((planStart.getTime()-base.getTime())/ratHour);
                int planEndHour=(int)((planEnd.getTime()-base.getTime())/ratHour);
                int startHour=(int)((start.getTime()-base.getTime())/ratHour);
                int endHour=(int)((end.getTime()-base.getTime())/ratHour);
                endHour=Math.min(planEndHour,endHour);//检索区间右侧，由二者最小值决定
                startHour=Math.max(planStartHour,startHour);//检索区间左侧，由二者最大值决定

                int nextStart=(startHour/24)*24;
                int nextEnd=(startHour/24+1)*24;
                int x1=0;
                int x2=0;
                while(nextStart<endHour){
                    if (nextEnd<endHour){
                        x1=(Math.max(nextStart,startHour));
                        x2=nextEnd;
                    }else {
                        x1=Math.max(nextStart,startHour);
                        x2=endHour;
                    }
                    x1=x1%24;
                    x2=x2%24;
                    if (x2==0)
                        x2=24;
                    sb.append("{x:");
                    sb.append(x1);
                    sb.append(",x2:");
                    sb.append(x2);
                    sb.append(",y:");
                    sb.append(pos);
                    sb.append("},");
                    nextStart=nextEnd;
                    nextEnd+=24;
                    pos++;
                }
            }
            if (list.size()>0)
                sb.deleteCharAt(sb.length()-1);
        }
        sb.append("]");
        return sb.toString();
    }

    /**
    *@Description: 新增资源任务 (预定资源)
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/2/29
    */
    public void bookResource(String resourceNo,Long nodeId,Date startTime,Date endTime,int useTime){

        ResourceTaskM resourceTaskM=new ResourceTaskM();
        resourceTaskM.setResourceNo(resourceNo);
        resourceTaskM.setNodeId(nodeId);
        resourceTaskM.setBookStart(startTime);
        resourceTaskM.setBookEnd(endTime);
        resourceTaskM.setUseTime(useTime);
        resourceTaskM.setWorkId(0L);//默认时，表示流程未发起

        //没有计划时，计划时间为预定时间，没有完成时，完成时间为计划时间）
        resourceTaskM.setPlanStart(startTime);
        resourceTaskM.setPlanEnd(endTime);
        resourceTaskM.setEndTime(endTime);
        resourceTaskM.setStartTime(startTime);

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
