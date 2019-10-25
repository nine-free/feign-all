package top.soft1010.feign;

import feign.*;

import java.util.Map;

/**
 * Created by zhangjifu on 19/10/25.
 */
public interface TestService {

    //直接url path中添加参数
    @RequestLine("GET /feign/get/{id}")
    String test1(@Param("id") Integer id);

    //url带参数
    @RequestLine("GET /feign/get?id={id}")
    String test2(@Param("id") Integer id);

    //add header 注意id: {id}中间的空格不能省略
    @RequestLine("GET /feign/getHeader")
    @Headers(value = {"id: {id}"})
    String test3(@Param("id") String id);

    //headerMap 添加多个header
    @RequestLine("GET /feign/getHeaders")
    String test4(@HeaderMap Map<String, Object> headers);

    //QueryMap 添加多个查询条件
    @RequestLine("GET /feign/queryMap")
    String test5(@QueryMap Map<String, Object> params);

    //指定json串 注意这里最外层的括号需要转义 很无语是不是
    @RequestLine("POST /feign/json")
    @Body("%7B\"id\":{id},\"name\":\"{name}\"%7D")
    @Headers("Content-Type: application/json")
    String test6(@Param("id") Integer id, @Param("name") String name);

    //这里直接将json字符串作为一个整体
    @RequestLine("POST /feign/json")
    @Body("{jsonBody}")
    @Headers("Content-Type: application/json")
    String test7(@Param("jsonBody") String jsonBody);

}
