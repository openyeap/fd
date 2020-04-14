package ltd.fdsa.demo.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "login")
public class ViewLogin {
	@ApiModelProperty(required=true, position= -10)
	@JsonProperty(value="user_name")
	private String userName;
	@ApiModelProperty(required=true, position= -1)
	private String password;
	@JsonProperty(value="validate_code")
	private String validateCode;
}
