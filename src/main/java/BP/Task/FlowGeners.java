package BP.Task;

import BP.En.EntitiesNo;
import BP.En.Entity;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-04-07 16:25
 **/
public class FlowGeners extends EntitiesNo {
    @Override
    public Entity getGetNewEntity() {
        return new FlowGener();
    }
}
