package BP.Judge.Condition;

import BP.Judge.JudgeCondition;
import BP.springCloud.entity.NodeTaskM;
import org.springframework.stereotype.Component;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-04-27 22:21
 **/
@Component
public class KongJudge implements JudgeCondition {
    @Override
    public boolean judge(NodeTaskM taskM) {
        return true;
    }
}
