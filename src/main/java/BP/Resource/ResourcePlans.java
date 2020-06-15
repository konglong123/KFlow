package BP.Resource;

import BP.En.EntitiesNo;
import BP.En.Entity;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-06-13 08:14
 **/
public class ResourcePlans extends EntitiesNo {
    @Override
    public Entity getGetNewEntity() {
        return new ResourcePlan();
    }
}
