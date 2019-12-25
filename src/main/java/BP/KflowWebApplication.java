package BP;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class KflowWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(KflowWebApplication.class, args);
    }

}
