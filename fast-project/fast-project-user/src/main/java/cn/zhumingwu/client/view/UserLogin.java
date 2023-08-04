package cn.zhumingwu.client.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserLogin {

    private String username;
    private String password;
    @JsonProperty("validate_code")
    private String validate_code;

}
