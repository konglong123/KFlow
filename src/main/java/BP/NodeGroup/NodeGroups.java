package BP.NodeGroup;

import BP.En.EntitiesNo;
import BP.En.Entity;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-09-14 11:19
 **/
public class NodeGroups extends EntitiesNo {
    @Override
    public Entity getGetNewEntity() {
        return new NodeGroup();
    }
}
