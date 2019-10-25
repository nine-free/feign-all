package top.soft1010.feign;

import feign.Feign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhangjifu on 19/10/25.
 */
public class FeignTest {

    static final Logger logger = LoggerFactory.getLogger(FeignTest.class);

    public static void main(String[] args) {
        TestService testService = Feign.builder().target(TestService.class, "http://127.0.0.1:8080");
        String ret = testService.test1(1);
        logger.info(ret);
    }
}
