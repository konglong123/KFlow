package BP.Nlp;

import com.hankcs.hanlp.corpus.document.sentence.word.Word;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;
import com.hankcs.hanlp.model.perceptron.PerceptronSegmenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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



}
