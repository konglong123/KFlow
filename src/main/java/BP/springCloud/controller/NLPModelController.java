package BP.springCloud.controller;

import BP.Nlp.NLPTool;
import BP.Nlp.NlpmodelService;
import BP.NodeGroup.NodeGroup;
import BP.NodeGroup.NodeGroupAttr;
import BP.NodeGroup.NodeGroups;
import BP.Sys.EnCfg;
import BP.WF.Flow;
import BP.WF.Flows;
import BP.WF.Template.FlowAttr;
import BP.springCloud.entity.Nlpmodel;
import BP.springCloud.tool.FeignTool;
import BP.springCloud.tool.Page;
import BP.springCloud.tool.PageTool;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hankcs.hanlp.mining.word2vec.DocVectorModel;
import com.hankcs.hanlp.mining.word2vec.Word2VecTrainer;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;
import com.hankcs.hanlp.model.perceptron.CWSTrainer;
import com.hankcs.hanlp.model.perceptron.PerceptronSegmenter;
import com.hankcs.hanlp.model.perceptron.PerceptronTrainer;
import com.hankcs.hanlp.model.perceptron.model.LinearModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Value("${hanlp.docFile}")
    private  String docFile;

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
        return null;
    }

    /**
    *@Description:  文档相似度模型中，增加文档
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/5/14
    */
    @RequestMapping("getNearestDoc")
    @ResponseBody
    public Object getNearestDoc(String doc){
        return queryWord2Document(doc);
    }



    /**
    *@Description: 根据abstracts查询相似文档，并返回list<JSONObject</>>
    *@Param:  JSONObject("score","abstracts")
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/5/14
    */
    private List<JSONObject> queryWord2Document(String doc){
        try {
            DocVectorModel docVectorModel = NLPTool.getDocVectorModel();
            if (docVectorModel == null) {
                docVectorModel = NLPTool.createDocVectorModel(word2vecModelFile, docFile);
            } else if (docVectorModel.size() == 0) {
                NLPTool.initDocVectorModelDocument(docFile);
            }
            List<Map.Entry<Integer, Float>> list=docVectorModel.nearest(doc,100);//查找相近的doc（默认10个，后期增加可选数量）
            List result= NLPTool.transToJson(list,docFile);
            return result;
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }

    @RequestMapping("addDoc")
    @ResponseBody
    public JSONObject addDoc(String doc){
        try {
            NLPTool.addDocumentToFile(doc,docFile);
        }catch (Exception  e){
            logger.error(e.getMessage());
        }

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
            nlpmodel.setTrainFile("D:/Springboot/nlp/pku98/199801-train.txt");
            nlpmodel.setTestFile("D:/Springboot/nlp/pku98/199801-test.txt");
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

    @RequestMapping("deletedModel")
    @ResponseBody
    public void delModel(HttpServletRequest request){
        String modelId=request.getParameter("modelId");
        nlpmodelService.deleteNlpmodel(Integer.parseInt(modelId));
    }

    @RequestMapping("updateW2VDocument")
    @ResponseBody
    public Object updateW2VDocument(){

        Map<String, Object> postBody = new HashMap<>();
        postBody.put("startPoint", 0);
        postBody.put("pageLength", 1000);
        postBody.put("abstracts", "");
        HttpEntity<Map> requestEntity = new HttpEntity<>(postBody, null);
        ResponseEntity<Page> resTemp = FeignTool.template.postForEntity("http://112.125.90.132:8082/es/getWFDsl", requestEntity, Page.class);
        Page pageResult=resTemp.getBody();
        Map<String, Object> jsonMap = new HashMap<>();//定义map
        List<Map> esData= pageResult.getData();
        List<String> fileList=new ArrayList<>();

        //添加流程模板信息
        for (Map item: esData){
            fileList.add((String) item.get("abstracts"));
        }

        //添加节点分组信息
        try {
            NodeGroups groups = new NodeGroups();
            groups.RetrieveAll();
            List<NodeGroup> groupList = groups.toList();
            for (NodeGroup group : groupList) {
                //模块分组
                if (group.GetValIntByKey(NodeGroupAttr.type) == 2) {
                    fileList.add(group.GetValStrByKey(NodeGroupAttr.abstracts));
                }
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }

        //同步数据到docFile中
        try {
            NLPTool.updateDocumentFile(docFile,fileList);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        jsonMap.put("total",fileList.size());
        return jsonMap;
    }


    /**
    *@Description: 语义检索流程模板
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/9/16
    */
    @RequestMapping("getWFMultiType")
    @ResponseBody
    public void getWFbyWord2(HttpServletRequest request,HttpServletResponse response){
        String type=request.getParameter("type");
        try {
            if (type.equals("1"))
                FeignTool.esQuery("http://112.125.90.132:8082/es/getWFDsl",request,response);
            else 
                word2Query(request,response);
                
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        
    }
    
    /**
    *@Description: word2vec查询流程 
    *@Param:  
    *@return:  
    *@Author: Mr.kong
    *@Date: 2020/5/14 
    */
    private void word2Query(HttpServletRequest request,HttpServletResponse response){
        String abstracts=request.getParameter("abstracts");
        List<JSONObject> data=queryWord2Document(abstracts);

        List<Object> result=new ArrayList<>();
        //查询流程信息
        try {
            Flows flows = new Flows();
            for (JSONObject item : data) {
                String doc = item.getString("abstracts");
                flows.Retrieve(FlowAttr.Note, doc);
                if (flows.size()>0) {
                    Flow flow=(Flow) flows.get(0);
                    item.put("mysqlId", flow.getNo());
                    item.put("name",flow.getName());
                    result.add(item);
                }

            }

            PageTool.TransToResultList(result,request,response);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }


    /**
    *@Description: 语义检索 流程片段（支持两种方式）
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/9/16
    */
    @RequestMapping("getNodeGroupMultiType")
    @ResponseBody
    public void getNodeGroupMultiType(HttpServletRequest request,HttpServletResponse response){
        /*String type=request.getParameter("type");
        try {
            if (type.equals("1"))
                FeignTool.esQuery("http://112.125.90.132:8082/es/getWFDsl",request,response);
            else
                queryNodeGroupByWord2(request,response);

        }catch (Exception e){
            logger.error(e.getMessage());
        }*/
    }

    /**
    *@Description: word2检索节点组
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/9/16
    */
    public List<JSONObject> queryNodeGroupByWord2(String abstracts){
        List<JSONObject> data=queryWord2Document(abstracts);
        List<JSONObject> result=new ArrayList<>();
        //查询流程信息
        try {
            NodeGroups groups = new NodeGroups();
            for (JSONObject item : data) {
                String doc = item.getString("abstracts");
                groups.Retrieve(NodeGroupAttr.abstracts, doc);
                if (groups.size() > 0) {
                    NodeGroup group = (NodeGroup) groups.get(0);
                    item.put("group", group);
                    item.put("score",item.getFloat("score"));
                    result.add(item);
                }

            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return result;
    }
}
