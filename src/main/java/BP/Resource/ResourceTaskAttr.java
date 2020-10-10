package BP.Resource;

/**
 * @program: kflow-web
 * @description: 资源任务属性
 * @author: Mr.Kong
 * @create: 2020-02-25 14:11
 **/
public class ResourceTaskAttr {
    //主键
    public static final String No = "No";

    //资源id(资源实例编码)
    public static final String ResourceId = "resource_id";

    //资源编码(资源类别编码)
    public static final String ResourceNo = "resource_no";

    //节点id（节点编码）
    public static final String NodeId = "node_Id";

    //资源方案编码
    public static final String PlanId="plan_id";

    //工作id（工作编码）
    public static final String TaskId = "task_id";

    public static final String UseTime = "use_time";

    //任务的开始时间（实际）
    public static final String StartTime = "start_time";

    //任务对结束时间（实际）
    public static final String EndTime = "end_time";

    //任务的预计开始时间
    public static final String BookStart = "book_start";

    //任务的预计结束时间
    public static final String BookEnd = "book_end";

    //任务的预计开始时间
    public static final String PlanStart = "plan_start";

    //任务的预计结束时间
    public static final String PlanEnd = "plan_end";

    //任务是否完成
    public static final String IsFinish = "is_finish";

    //任务是否经过优化调度
    public static final String IsPlan = "is_plan";

    public static final String UseNum = "use_num";

}
