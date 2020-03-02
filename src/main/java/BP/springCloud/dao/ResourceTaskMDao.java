package BP.springCloud.dao;

import BP.springCloud.entity.ResourceTaskM;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @program: basic-services
 * @description:
 * @author: Mr.Kong
 * @create: 2019-12-18 10:03
 **/
@Repository
public interface ResourceTaskMDao {

    List getResourceTaskByPlanTime(@Param("resTask")ResourceTaskM resourceTaskM,String startTime,String endTime);

    void updateResourceTask(@Param("resTask") ResourceTaskM resourceTaskM);

    void addResourceTask(@Param("resTask") ResourceTaskM resourceTaskM);

    void deleteResourceTask(String no);

}
