package BP.WF.Port;

import BP.En.EntitiesNoName;
import BP.En.Entity;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-02-23 12:22
 **/
public class Resources extends EntitiesNoName {
    @Override
    public Entity getGetNewEntity()
    {
        return new Resource();
    }
}
