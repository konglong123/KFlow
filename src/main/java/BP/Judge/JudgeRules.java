package BP.Judge;

import BP.En.EntitiesNo;
import BP.En.Entity;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-04-25 16:32
 **/
public class JudgeRules extends EntitiesNo {
    @Override
    public Entity getGetNewEntity() {
        return new JudgeRule();
    }
}
