package top.soft1010.feign.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;

/**
 * Created by zhangjifu on 19/10/25.
 */

@SpringBootApplication
@EnableFeignClients
@ComponentScan(basePackages = "top.soft1010")
public class TestAppApplication {

    final static Logger logger = LoggerFactory.getLogger(TestAppApplication.class);

    public static void main(String[] args) {
        // 启动Sprign Boot
        ApplicationContext ctx = SpringApplication.run(TestAppApplication.class, args);
        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            logger.info(beanName);
        }
    }
}
