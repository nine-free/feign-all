# feign-all

## feign是什么(what)
>feign是java实现的一个声明式的http客户端，
## feign的使用(how)
#### 简单使用
maven pom.xml
```
        <dependency>
            <groupId>com.netflix.feign</groupId>
            <artifactId>feign-core</artifactId>
            <version>${feign.version}</version>
        </dependency>
```
定义http调用接口
```
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

```
调用方法
```
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
```
可以看到使用feign调用http接口相当简单，没有多余的代码

#### feign的丰富功能
```
import feign.*;
import feign.codec.Decoder;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import org.junit.Test;
import top.soft1010.feign.TestService;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangjifu on 19/10/25.
 */
public class ComplexFeignTest {

    @Test
    public void test() {
        /**
         * 包含更多
         * 拦截器 编码 解码 options(超时时间) 重试 日志
         */
        List<RequestInterceptor> requestInterceptors = new ArrayList<RequestInterceptor>();
        requestInterceptors.add(new MyRequestInterceptor());
        TestService testService = Feign.builder().
                requestInterceptors(requestInterceptors).
                decoder(new MyDecoder()).
                encoder(new MyEncoder()).
                options(new Request.Options(10000, 60000)).
                retryer(new MyRetryer(1000, 10000, 3)).
                logger(new MyLogger()).
                logLevel(Logger.Level.BASIC).
                target(TestService.class, "http://127.0.0.1:8080/");
        testService.test1(1);

    }


    static class MyRequestInterceptor implements RequestInterceptor {
        public void apply(RequestTemplate template) {
            System.out.println("MyRequestInterceptor  " + System.currentTimeMillis() + " 发起请求" + template.url());
        }
    }

    static class MyEncoder extends Encoder.Default {
        public void encode(Object o, Type type, RequestTemplate requestTemplate) throws EncodeException {
            System.out.println("MyEncoder " + type.toString() + "  " + o.toString());
        }
    }

    static class MyDecoder extends Decoder.Default {
        public Object decode(Response response, Type type) throws IOException, FeignException {
            if (response.status() == 200) {
                System.out.println("MyDecoder 请求成功");
            }
            return super.decode(response, type);
        }
    }

    static class MyLogger extends Logger {
        @Override
        protected void log(String configKey, String format, Object... args) {
            System.out.println(String.format("MyLogger " + methodTag(configKey) + format + "%n", args));
        }

        @Override
        protected void logRequest(String configKey, Level logLevel, Request request) {
            if (logLevel.equals(Level.BASIC)) {
                super.logRequest(configKey, logLevel, request);
            }
        }
    }

    static class MyRetryer implements Retryer {

        int maxAttempts;
        long period;
        long maxPeriod;
        int attempt;
        long sleptForMillis;

        public MyRetryer(long period, long maxPeriod, int maxAttempts) {
            this.period = period;
            this.maxAttempts = maxAttempts;
            this.maxPeriod = maxPeriod;
        }

        protected long currentTimeMillis() {
            return System.currentTimeMillis();
        }

        public void continueOrPropagate(RetryableException e) {
            if (this.attempt++ >= this.maxAttempts) {
                throw e;
            } else {
                long interval;
                if (e.retryAfter() != null) {
                    interval = e.retryAfter().getTime() - this.currentTimeMillis();
                    if (interval > this.maxPeriod) {
                        interval = this.maxPeriod;
                    }

                    if (interval < 0L) {
                        return;
                    }
                } else {
                    interval = this.nextMaxInterval();
                }

                try {
                    Thread.sleep(interval);
                } catch (InterruptedException var5) {
                    Thread.currentThread().interrupt();
                }

                this.sleptForMillis += interval;
            }
        }

        long nextMaxInterval() {
            long interval = (long) ((double) this.period * Math.pow(1.5D, (double) (this.attempt - 1)));
            return interval > this.maxPeriod ? this.maxPeriod : interval;
        }

        public Retryer clone() {
            System.out.println("MyRetryer clone");
            return new MyRetryer(this.period, this.maxPeriod, this.maxAttempts);
        }
    }

}
```
可以看到feign调用http接口，支持拦截器，编解码，重试等丰富的功能

## feign的实现原理(why)
feign的核心jar包只有一个 **feign-core-8.18.0.jar**
看一下整个jar包下面的类结构，相对来说比较简单
![image](http://soft1010.top/img/feign-class.jpeg)

#### 通源码解析实现原理
```
                Feign.builder().
                requestInterceptors(requestInterceptors).
                decoder(new MyDecoder()).
                encoder(new MyEncoder()).
                options(new Request.Options(10000, 60000)).
                retryer(new MyRetryer(1000, 10000, 3)).
                logger(new MyLogger()).
                logLevel(Logger.Level.BASIC).
                target(TestService.class, "http://127.0.0.1:8080/");     
```
1. 通过**构造器模式**构造Feign.Builder对象。
2. target()方法最后调用了ReflectiveFeign.newInstance()方法
3. 核心代码 通过**动态代理**将feign模板化的接口实例化
```
    InvocationHandler handler = factory.create(target, methodToHandler);
    T proxy = (T) Proxy.newProxyInstance(target.type().getClassLoader(), new Class<?>[]{target.type()}, handler);
```
4. 根据动态代理的原理，InvocationHandler这个接口的实现既是具体调用逻辑所在
 ```
 static class FeignInvocationHandler implements InvocationHandler {

    private final Target target;
    private final Map<Method, MethodHandler> dispatch;

    FeignInvocationHandler(Target target, Map<Method, MethodHandler> dispatch) {
      this.target = checkNotNull(target, "target");
      this.dispatch = checkNotNull(dispatch, "dispatch for %s", target);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      if ("equals".equals(method.getName())) {
        try {
          Object
              otherHandler =
              args.length > 0 && args[0] != null ? Proxy.getInvocationHandler(args[0]) : null;
          return equals(otherHandler);
        } catch (IllegalArgumentException e) {
          return false;
        }
      } else if ("hashCode".equals(method.getName())) {
        return hashCode();
      } else if ("toString".equals(method.getName())) {
        return toString();
      }
      //看看这个dispatct.get（）是如何实现的？
      return dispatch.get(method).invoke(args);
    }
```
```
@Override
  public Object invoke(Object[] argv) throws Throwable {
    RequestTemplate template = buildTemplateFromArgs.create(argv);
    Retryer retryer = this.retryer.clone();
    while (true) {
      try {
        return executeAndDecode(template);
      } catch (RetryableException e) {
        retryer.continueOrPropagate(e);
        if (logLevel != Logger.Level.NONE) {
          logger.logRetry(metadata.configKey(), logLevel);
        }
        continue;
      }
    }
  }

```
最终调用了lient类中C的默认实现
![image](http://soft1010.top/img/feign-class-1.jpg)

**结论：feign最核心的逻辑还是利用了放射&动态代理设计模式最终还是调用了java API实现http接口的调用。**

## feign与其他http客户端对比
直接通过代码比较一下几个http调用
#### java原生调用http接口
```
public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
```
#### httpclient
```
 CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        URI uri = new URIBuilder().
                setScheme("https").
                setHost("www.google.com").
                setPath("/search").
                setParameter("q", "张").
                setParameter("btnG","Google Search").
                setParameter("oq","").
                setParameter("aq","f").
                build();
        CloseableHttpResponse closeableHttpResponse = null;
        try {
            HttpGet httpGet = new HttpGet(uri);
            System.out.println(httpGet.getURI().toString());
            closeableHttpResponse = closeableHttpClient.execute(httpGet);
            System.out.println(closeableHttpResponse.getStatusLine().getStatusCode());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (closeableHttpResponse != null) {
                try {
                    closeableHttpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
```
#### okhttp
```
        String url = "http://127.0.0.1:8080/feign/get/1";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("1", "2")
                .build();
        String ret = "";
        try (Response response = client.newCall(request).execute()) {
            ret = response.body().string();
        }
        System.out.println(ret);
```
不同的http调用的对比
|原生API|httpClient|okhttp|feign|
|---|---|---|---|
|代码量最多|对比代码量比原生好很多，支持所有http调用|相对httpclient代码量更少|代码量最少，书写最简单，但是仅支持最常用的调用|

## feign在spring boot中的应用


## 延伸

#### 反射

#### 动态代理
