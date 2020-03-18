package BP;

import BP.Tools.BeanTool;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;

//@EnableEurekaClient
@SpringBootApplication
@MapperScan("BP.springCloud.dao")
public class Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Application.class, args);
        BeanTool.applicationContext = applicationContext;
    }

}
