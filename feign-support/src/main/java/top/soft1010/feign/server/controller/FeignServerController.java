package top.soft1010.feign.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import top.soft1010.feign.server.JsonPo;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Created by zhangjifu on 19/10/25.
 */
@RestController
@RequestMapping(value = "/feign")
public class FeignServerController {

    Logger logger = LoggerFactory.getLogger(FeignServerController.class);

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public Object test1(@PathVariable(value = "id") Integer id, HttpServletRequest request) {
        return "id:" + id;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Object test2(@RequestParam(value = "id") Integer id) {
        return "id:" + id;
    }

    @RequestMapping(value = "/getHeader", method = RequestMethod.GET)
    public Object test3(HttpServletRequest request) {
        Enumeration<String> enumeration = request.getHeaderNames();
        for (; enumeration.hasMoreElements(); ) {
            logger.info(enumeration.nextElement());
        }
        return "id:" + request.getHeader("id");
    }

    @RequestMapping(value = "/getHeaders", method = RequestMethod.GET)
    public Object test4(HttpServletRequest request) {
        Enumeration<String> enumeration = request.getHeaderNames();
        for (; enumeration.hasMoreElements(); ) {
            logger.info(enumeration.nextElement());
        }
        return "id:" + request.getHeader("header1");
    }

    @RequestMapping(value = "/queryMap", method = RequestMethod.GET)
    public Object test5(HttpServletRequest request) {
        logger.info(request.getQueryString());
        return request.getQueryString();
    }

    @RequestMapping(value = "json", method = RequestMethod.POST)
    @ResponseBody
    public JsonPo test6(@RequestBody JsonPo jsonPo) {
        logger.info(jsonPo != null ? jsonPo.getId() + "|" + jsonPo.getName() : null);
        jsonPo.setId(jsonPo != null ? jsonPo.getId() + 1 : null);
        return jsonPo;
    }

}
