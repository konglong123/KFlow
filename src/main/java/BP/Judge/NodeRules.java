package BP.Judge;

import BP.En.EntitiesNo;
import BP.En.Entity;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-04-29 17:01
 **/
public class NodeRules extends EntitiesNo {
    @Override
    public Entity getGetNewEntity(){
        return new NodeRule();
    }
}
