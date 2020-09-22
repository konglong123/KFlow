package BP.Resource;

import BP.En.EntitiesNoName;
import BP.En.Entity;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-09-21 11:16
 **/
public class ResourceItems extends EntitiesNoName {
    @Override
    public Entity getGetNewEntity()
    {
        return new ResourceItem();
    }
}
