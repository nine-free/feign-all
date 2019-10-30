package top.soft1010.feign.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by zhangjifu on 19/10/28.
 */
@FeignClient(name = "test-feign-client2", url = "http://127.0.0.1:8080/")
public interface FeignConsumer2 {

    @RequestMapping(value = "/feign/get/{id}")
    String test1(@PathVariable(value = "id") Integer id);

    @RequestMapping(value = "/feign/get/")
    String test2(@RequestParam(value = "id") Integer id);

}
