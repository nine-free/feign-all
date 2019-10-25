import com.google.gson.Gson;
import feign.Feign;
import org.junit.Test;
import top.soft1010.feign.JsonPo;
import top.soft1010.feign.TestService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangjifu on 19/10/25.
 */
public class FeignTestServiceTest {

    @Test
    public void test1() {
        TestService testService = Feign.builder().target(TestService.class, "http://127.0.0.1:8080");
        String ret = testService.test1(1);
        System.out.println(ret);
    }

    @Test
    public void test2() {
        TestService testService = Feign.builder().target(TestService.class, "http://127.0.0.1:8080");
        String ret = testService.test2(1);
        System.out.println(ret);
    }

    @Test
    public void test3() {
        TestService testService = Feign.builder().target(TestService.class, "http://127.0.0.1:8080");
        String ret = testService.test3("2");
        System.out.println(ret);
    }

    @Test
    public void test4() {
        TestService testService = Feign.builder().target(TestService.class, "http://127.0.0.1:8080");
        Map<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("header1", "header1Value");
        headerMap.put("header2", "header2Value");
        String ret = testService.test4(headerMap);
        System.out.println(ret);
    }

    @Test
    public void test5() {
        TestService testService = Feign.builder().target(TestService.class, "http://127.0.0.1:8080");
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("param1", "params1Value");
        queryMap.put("param2", "params2Value");
        String ret5 = testService.test5(queryMap);
        System.out.println(ret5);
    }

    @Test
    public void test6() {
        TestService testService = Feign.builder().target(TestService.class, "http://127.0.0.1:8080");
        String ret6 = testService.test6(1, "zhang");
        System.out.println(ret6);

    }

    @Test
    public void test7() {
        TestService testService = Feign.builder().target(TestService.class, "http://127.0.0.1:8080");
        JsonPo jsonPo = new JsonPo();
        jsonPo.setName("zhang");
        jsonPo.setId(1);
        String ret7 = testService.test7(new Gson().toJson(jsonPo));
        System.out.println(ret7);
    }
}
