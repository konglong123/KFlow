package BP.Nlp;

import BP.En.EntityNo;
import BP.En.Map;
import BP.springCloud.tool.FeignTool;
import org.springframework.util.StringUtils;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-11-18 20:39
 **/
public class SegmentModel extends EntityNo {
    public SegmentModel() {

    }

    public SegmentModel(String _no) throws Exception {
        if (_no == null || _no.equals("")) {
            throw new RuntimeException(this.getEnDesc() + "@对表["
                    + this.getEnDesc() + "]进行查询前必须指定编号。");
        }

        this.setNo(_no);
        if (this.Retrieve() == 0) {
            throw new RuntimeException("@没有"
                    + this.get_enMap().getPhysicsTable() + ", No = " + getNo()
                    + "的记录。");
        }
    }

    @Override
    public Map getEnMap() {
        if (this.get_enMap() != null) {
            return this.get_enMap();
        }
        //主键一定要采用AddTBStringPk,并且列为No（注意大小写）

        Map map = new Map("nlpmodel", "模型");
        map.AddTBStringPK(NlpModelAttr.No, null, "ID", true, true, 0, 50, 50);
        map.AddTBInt(NlpModelAttr.NumThreads, 1, "线程数", true, false);
        map.AddTBInt(NlpModelAttr.Iterations, 1, "迭代次数", true, false);
        map.AddDDLSysEnum(NlpModelAttr.ModelType, 2, "模型类型", true, true, NlpModelAttr.ModelType, "@1=词向量@2=分词");
        map.AddTBDateTime(NlpModelAttr.CreateTime, "2000-01-01 00:00:00", "创建时间", true, false);
        map.AddTBString(NlpModelAttr.ModelFile, null, "模型文件", true, true, 0, 50, 50);
        map.AddTBString(NlpModelAttr.TestFile, null, "测试数据", true, false, 0, 50, 50);
        map.AddTBString(NlpModelAttr.TrainFile, null, "训练数据", true, false, 0, 50, 50);
        map.AddTBFloat(NlpModelAttr.CompressRate, 0.1f, "压缩比", true, false);

        return map;
    }

    @Override
    protected boolean beforeInsert() throws Exception {
        //设置基本信息
        long id= FeignTool.getSerialNumber("BP.Nlp.NlpModel");
        this.SetValByKey(NlpModelAttr.No,id);

        return super.beforeInsert();
    }
}
