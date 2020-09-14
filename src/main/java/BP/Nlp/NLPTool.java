package BP.Nlp;

import com.alibaba.fastjson.JSONObject;
import com.hankcs.hanlp.corpus.document.sentence.word.Word;
import com.hankcs.hanlp.mining.word2vec.DocVectorModel;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;
import com.hankcs.hanlp.model.perceptron.PerceptronSegmenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: basic-services
 * @description:
 * @author: Mr.Kong
 * @create: 2020-04-10 14:19
 **/
public class NLPTool {

    private static Logger logger = LoggerFactory.getLogger(NLPTool.class);

    private static  PerceptronSegmenter perceptronSegmenter;

    private static WordVectorModel wordVectorModel;

    private static DocVectorModel docVectorModel;


    public static PerceptronSegmenter getPerceptronSegmenter(){
        return perceptronSegmenter;
    }

    public static PerceptronSegmenter createPerceptronSegment(String modelFile){
        try {
            perceptronSegmenter = new PerceptronSegmenter(modelFile);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return perceptronSegmenter;
    }

     public static WordVectorModel getWordVectorModel(){
        return wordVectorModel;
     }
    public static WordVectorModel createWordVectorModel(String modelFile){
        try {
            wordVectorModel = new WordVectorModel(modelFile);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return wordVectorModel;
    }

    public static DocVectorModel getDocVectorModel(){
        return docVectorModel;
    }

    public static DocVectorModel createDocVectorModel(String modelFile){
        try {
            wordVectorModel = new WordVectorModel(modelFile);
            docVectorModel=new DocVectorModel(wordVectorModel);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return docVectorModel;
    }

    public static DocVectorModel createDocVectorModel(String modelFile,String docFile) throws Exception{

        wordVectorModel = new WordVectorModel(modelFile);
        docVectorModel=new DocVectorModel(wordVectorModel);
        initDocVectorModelDocument(docFile);

        return docVectorModel;
    }

    //初始化文档相似度模型中文档内容
    public static void initDocVectorModelDocument(String docFile) throws Exception{
        FileInputStream fr = new FileInputStream(docFile);
        InputStreamReader isr=new InputStreamReader(fr, "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String line="";

        int i=0;
        while ((line = br.readLine()) != null) {
            if (line.equals(""))
                continue;
            docVectorModel.addDocument(i,line);
            i++;
        }
        br.close();
        fr.close();
    }

    //在docFile中增加文档
    public static void addDocumentToFile(String doc,String docFile) throws Exception{
        BufferedWriter bw = new BufferedWriter(new FileWriter(docFile,true));
        bw.write(doc);
        bw.newLine();
        bw.flush();
        bw.close();
    }
    //获取文件的特定多行信息
    public static List<String> getFileByIndex(String docFile, List<Integer> positions) throws Exception{
        FileInputStream fr = new FileInputStream(docFile);
        InputStreamReader isr=new InputStreamReader(fr, "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String line="";
        List<String> fileList=new ArrayList<>();
        while ((line = br.readLine()) != null) {
            fileList.add(line);
        }

        List<String> result=new ArrayList<>(positions.size());
        for (Integer index:positions){
            result.add(fileList.get(index));
        }
        return result;
    }


/**
*@Description: 根据文档相似度组装查询结果
*@Param:
*@return:
*@Author: Mr.kong
*@Date: 2020/5/14
*/
    public static List<JSONObject> transToJson(List<Map.Entry<Integer, Float>> list,String docFile) throws Exception{
        List<Integer> positions=new ArrayList<>(list.size());
        for (Map.Entry<Integer,Float> entry:list){
            positions.add(entry.getKey());
        }
        List<String> documents=getFileByIndex(docFile,positions);

        //组装document和相似度评分返回
        List<JSONObject> result=new ArrayList<>(list.size());
        int i=0;
        for (Map.Entry<Integer,Float> entry:list){
            JSONObject item=new JSONObject();
            item.put("score",entry.getValue());
            item.put("abstracts",documents.get(i));
            result.add(item);
            i++;
        }
        return result;
    }

    public static void updateDocumentFile(String docFile,List<String> documents) throws Exception{
        BufferedWriter bw = new BufferedWriter(new FileWriter(docFile,false));
        for (String doc:documents) {
            bw.write(doc);
            bw.newLine();
        }
        bw.flush();
        bw.close();
    }


}
