package BP.Tools;

import org.springframework.context.ConfigurableApplicationContext;

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
}
