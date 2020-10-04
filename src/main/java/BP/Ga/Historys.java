package BP.Ga;

import BP.En.EntitiesNo;
import BP.En.Entity;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-10-04 16:29
 **/
public class Historys extends EntitiesNo {
    @Override
    public Entity getGetNewEntity() {
        return new History();
    }
}
