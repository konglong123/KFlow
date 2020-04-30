package BP.Nlp;

import BP.Sys.EnCfg;
import BP.springCloud.entity.Nlpmodel;
import BP.springCloud.tool.FeignTool;
import BP.springCloud.tool.PageTool;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hankcs.hanlp.mining.word2vec.Word2VecTrainer;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;
import com.hankcs.hanlp.model.perceptron.CWSTrainer;
import com.hankcs.hanlp.model.perceptron.PerceptronSegmenter;
import com.hankcs.hanlp.model.perceptron.PerceptronTrainer;
import com.hankcs.hanlp.model.perceptron.model.LinearModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: basic-services
 * @description:
 * @author: Mr.Kong
 * @create: 2020-04-09 17:13
 **/
@Controller
@RequestMapping("NLPModel")
public class NLPModelController {

    private static Logger logger = LoggerFactory.getLogger(NLPModelController.class);

    @Resource
    private NlpmodelService nlpmodelService;

    @Value("${hanlp.segmentModelFile}")
    private  String segmentModelFile;

    @Value("${hanlp.segmentTrainingFile}")
    private  String segmentTrainingFile;

    @Value("${hanlp.word2vecModelFile}")
    private  String word2vecModelFile;

    @Value("${hanlp.dir}")
    private String userFileDir;

    @RequestMapping("insert")
    @ResponseBody
    public Long insertNLPModel(@RequestBody Nlpmodel nlpmodel){
        return nlpmodelService.insertNlpmodel(nlpmodel);
    }


    /**
     *@Description: 训练hanlp中word2vec模型，
     *@Param:
     *@return:
     *@Author: Mr.kong
     *@Date: 2020/4/9
     */
    @ResponseBody
    @RequestMapping("trainWord2vec")
    public JSONObject trainWord2vec(@RequestBody Nlpmodel nlpmodel){
        nlpmodel.setModelFile(userFileDir+"/"+nlpmodel.getModelFile()+".txt");
        Word2VecTrainer trainerBuilder = new Word2VecTrainer();
        WordVectorModel wordVectorModel = trainerBuilder.train(nlpmodel.getTrainFile(), nlpmodel.getModelFile());
        nlpmodelService.insertNlpmodel(nlpmodel);
        wordVectorModel.nearest("中国");
        return null;
    }


    /**
     *@Description:  查询词向量
     *@Param:
     *@return:
     *@Author: Mr.kong
     *@Date: 2020/4/9
     */
    @RequestMapping("getWordVector")
    @ResponseBody
    public JSONObject getWordVector(HttpServletRequest request){
        return null;
    }

    //查询词向量相近词（语义相近词）
    @RequestMapping("getNearestWords")
    @ResponseBody
    public JSONObject getNearestWords(HttpServletRequest request){
        return null;
    }


    /**
    *@Description:
    *@Param:  
    *@return:  
    *@Author: Mr.kong
    *@Date: 2020/4/16 
    */
    @RequestMapping("getNlpModelList")
    public void getNlpModelList(HttpServletRequest request, HttpServletResponse response){
        try {
            Nlpmodel con=new Nlpmodel();
            String id=request.getParameter("id");
            if (!StringUtils.isEmpty(id))
                con.setId(Long.parseLong(id));
            
            String name=request.getParameter("name");
            if (!StringUtils.isEmpty(name))
                con.setName(name);

            String trainFile=request.getParameter("trainFile");
            if (!StringUtils.isEmpty(trainFile))
                con.setTrainFile(trainFile);

            String modelFile=request.getParameter("modelFile");
            if (!StringUtils.isEmpty(modelFile))
                con.setModelFile(modelFile);

            String type=request.getParameter("type");
            if (!StringUtils.isEmpty(type))
                con.setModelType(Integer.parseInt(type));

            List list = nlpmodelService.findNlpmodelList(con);
            PageTool.TransToResultList(list, request, response);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    @RequestMapping("learnWord")
    @ResponseBody
    public List CWSTrainer(HttpServletRequest request) throws Exception{
        String sentence=request.getParameter("sentence");
        PerceptronSegmenter segment= NLPTool.getPerceptronSegmenter();
        if (segment==null)
            segment=NLPTool.createPerceptronSegment(segmentModelFile);
        segment.learn(sentence);
        List list=segment.segment(sentence);
        return list;
    }

    @RequestMapping("segmentSentence")
    @ResponseBody
    public List segmentSentence(HttpServletRequest request){
        PerceptronSegmenter segmenter=NLPTool.getPerceptronSegmenter();
        if (segmenter==null)
            segmenter=NLPTool.createPerceptronSegment(segmentModelFile);
        String sentence=request.getParameter("sentence");
        List list=segmenter.segment(sentence);
        return list;
    }

    @RequestMapping("saveNLPModel")
    @ResponseBody
    public Nlpmodel saveSegmentModel(HttpServletRequest request){
        String modelType=request.getParameter("modelType");
        JSONObject config = getSysNlpModelJson();
        if (modelType==null)
            return null;

        if (modelType.equals("2")) {
            PerceptronSegmenter segment = NLPTool.getPerceptronSegmenter();
            LinearModel model = segment.getModel();
            try {
                String id = config.getString("segment");
                Nlpmodel nlpmode = nlpmodelService.getNlpmodel(Long.parseLong(id));
                nlpmodelService.updateNlpmodel(nlpmode);
                String modelFile = nlpmode.getModelFile();
                model.save(modelFile);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }else if (modelType.equals("1")){

        }
        return null;
    }

    //训练结构化感知机分词模型
    //默认压缩比0.1，最大迭代次数50，线程为本机内核数
    @RequestMapping("trainSegmentModel")
    @ResponseBody
    public JSONObject trainCWS(@RequestBody Nlpmodel nlpmodel){
        try {
            PerceptronTrainer trainer = new CWSTrainer();
            initSegmentNlpModel(nlpmodel);
            PerceptronTrainer.Result modelResult=trainer.train(nlpmodel.getTrainFile(), nlpmodel.getTestFile(),nlpmodel.getModelFile(),nlpmodel.getCompressRate(),nlpmodel.getIterations(),nlpmodel.getNumThreads());
            nlpmodel.setCorrectRate(modelResult.getAccuracy());

            //对结果进行去小数点后2位
            List<Double> points=modelResult.getProgress();
            int len=points.size()-1;
            List<Float> pointsF=new ArrayList<>(len);//最后一个是平均准确率
            for (int i=0;i<len;i++){
                pointsF.add(Float.parseFloat(points.get(i).toString().substring(0,5)));
            }

            nlpmodel.setContext(JSON.toJSONString(pointsF));
            nlpmodelService.insertNlpmodel(nlpmodel);
            JSONObject result=new JSONObject();
            result.put("accuracy",modelResult.getAccuracy());
            result.put("message",modelResult.getMessage());
            result.put("progress",pointsF);
            return result;
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }

    @RequestMapping("loadModel")
    @ResponseBody
    public JSONObject loadModel(HttpServletRequest request){
        String modelId=request.getParameter("modelId");
        Nlpmodel nlpmodel=nlpmodelService.getNlpmodel(Long.parseLong(modelId));
        if (nlpmodel!=null){
            String modelFile=nlpmodel.getModelFile();
            //更新系统配置
            EnCfg enCfg=new EnCfg();
            enCfg.setNo("System.NLPModel");
            try {
                enCfg.RetrieveFromDBSources();
                String cur = enCfg.getGroupTitle();
                String[] curAttr = cur.split("@");
                if (nlpmodel.getModelType() == 2){
                    cur="segment=" + modelId+cur.substring(curAttr[0].length());
                    //加载新模型
                    NLPTool.createPerceptronSegment(modelFile);
                }else {
                    cur=cur.substring(0,curAttr[0].length()+1)+"word2vec=" + modelId;
                    //加载新模型
                    NLPTool.createWordVectorModel(modelFile);
                }
                enCfg.setGroupTitle(cur);
                enCfg.Update();
            }catch (Exception e){
                logger.error(e.getMessage());
            }
        }
        return null;
    }


    //初始化nlp分词训练模型入参
    private  void initSegmentNlpModel(Nlpmodel nlpmodel){
        if (StringUtils.isEmpty(nlpmodel.getTrainFile()))
            nlpmodel.setTrainFile(segmentTrainingFile);

        if (StringUtils.isEmpty(nlpmodel.getModelFile()))
            nlpmodel.setModelFile(userFileDir+segmentModelFile);
        else
            nlpmodel.setModelFile(userFileDir+"/"+nlpmodel.getModelFile()+ FeignTool.getSerialNumber("NlpModelName") +".bin");

        if (nlpmodel.getIterations()==0)
            nlpmodel.setIterations(50);

        if (nlpmodel.getCompressRate()==null)
            nlpmodel.setCompressRate(0.1f);

        int num=Runtime.getRuntime().availableProcessors();
        if (nlpmodel.getNumThreads()==0||nlpmodel.getNumThreads()>num)
            nlpmodel.setNumThreads(num);

        if (StringUtils.isEmpty(nlpmodel.getTestFile()))
            nlpmodel.setTestFile(nlpmodel.getTrainFile());
    }

    /**
    *@Description: 获取系统中采用的nlpModel 
    *@Param:  
    *@return:  
    *@Author: Mr.kong
    *@Date: 2020/4/16 
    */
    @RequestMapping("getSysNlpModel")
    @ResponseBody
    public JSONObject getSysNlpModel() throws Exception{
        return getSysNlpModelJson();
    }

    private JSONObject getSysNlpModelJson(){
        JSONObject config=new JSONObject();
        try {
            EnCfg enCfg = new EnCfg();
            enCfg.setNo("System.NLPModel");
            enCfg.RetrieveFromDBSources();
            String[] configAttr=enCfg.getGroupTitle().split("@");
            config.put("segment",configAttr[0].substring(8));
            config.put("word2vec",configAttr[1].substring(9));
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return config;
    }

    @RequestMapping("getModelTrainData")
    @ResponseBody
    public JSONObject getModelTrainData(HttpServletRequest request){
        String modelId=request.getParameter("modelId");
        Nlpmodel nlpmodel=nlpmodelService.getNlpmodel(Long.parseLong(modelId));
        String context=nlpmodel.getContext();
        JSONObject result=new JSONObject();
        result.put("progress",context);
        return result;
    }

}
