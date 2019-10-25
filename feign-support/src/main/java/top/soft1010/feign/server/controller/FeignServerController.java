package top.soft1010.feign.server.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhangjifu on 19/10/25.
 */
@RestController
@RequestMapping(value = "/feign")
public class FeignServerController {

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public Object test1(@PathVariable(value = "id") Integer id) {
        return "id:" + id;
    }
}
