package BP.NodeGroup;

import BP.En.EntitiesNo;
import BP.En.Entity;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-09-16 17:31
 **/
public class ComposeGroups extends EntitiesNo {
    @Override
    public Entity getGetNewEntity() {
        return new ComposeGroup();
    }
}
