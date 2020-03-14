package BP.Task;

import BP.En.EntitiesNo;
import BP.En.Entity;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-03-05 10:19
 **/
public class NodeTasks extends EntitiesNo {
    @Override
    public Entity getGetNewEntity() {
        return new NodeTask();
    }
}
