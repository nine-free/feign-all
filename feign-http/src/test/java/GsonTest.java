import com.google.gson.Gson;
import top.soft1010.feign.JsonPo;

/**
 * Created by zhangjifu on 19/10/25.
 */
public class GsonTest {

    public static void main(String[] args) {
        JsonPo jsonPo = new JsonPo();
        jsonPo.setId(1);
        jsonPo.setName("zhang");
        String str = new Gson().toJson(jsonPo);
        System.out.println(str);
    }
}
