package top.soft1010.feign;

import feign.Param;
import feign.RequestLine;

/**
 * Created by zhangjifu on 19/10/25.
 */
public interface TestService {

    @RequestLine("GET /feign/get/{id}")
    String test1(@Param("id") Integer id);
}
