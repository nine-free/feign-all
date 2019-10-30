import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.soft1010.feign.client.FeignConsumer;
import top.soft1010.feign.client.TestAppApplication;

/**
 * Created by zhangjifu on 19/10/28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TestAppApplication.class)
public class SpringFeignTest {

    @Autowired
    FeignConsumer feignConsumer;

    @Test
    public void test() {
        System.out.println(feignConsumer.test1(1));
    }

    @Test
    public void test2() {
        System.out.println(feignConsumer.test2(1));
    }
}
