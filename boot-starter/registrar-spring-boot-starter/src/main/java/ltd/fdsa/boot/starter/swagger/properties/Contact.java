package ltd.fdsa.boot.starter.swagger.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname Contact
 * @Description TODO
 * @Date 2019/12/16 14:28
 * @Author 高进
 */
@Data
public class Contact {
    private String name = "";
    private String url = "";
    private String email = "";
}
