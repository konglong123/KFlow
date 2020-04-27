package BP.Tools;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-03-16 22:10
 **/
public class BeanTool {
    public static ConfigurableApplicationContext applicationContext;

    public static <T> T getBean(Class<T> c){
        return applicationContext.getBean(c);
    }

    public static <T> T getBean(Class<T> c,String beanId){
        Map<String, T> beans=applicationContext.getBeansOfType(c);
        return beans.get(beanId);
    }

    public static  <T> Map<String,T> getBeans(Class<T> c){
        return applicationContext.getBeansOfType(c);
    }
}
