package BP.Project;

import BP.En.EntitiesNo;
import BP.En.Entity;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-08-26 18:42
 **/
public class ProjectTrees extends EntitiesNo {
    @Override
    public Entity getGetNewEntity() {
        return new ProjectTree();
    }
}
