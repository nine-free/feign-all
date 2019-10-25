package top.soft1010.feign.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

/**
 * Created by zhangjifu on 19/10/25.
 */
@SpringBootApplication
public class ServerApplication {

    final static Logger logger = LoggerFactory.getLogger(ServerApplication.class);

    public static void main(String[] args) {
        // 启动Sprign Boot
        ApplicationContext ctx = SpringApplication.run(ServerApplication.class, args);
        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            logger.info(beanName);
        }
    }
}
