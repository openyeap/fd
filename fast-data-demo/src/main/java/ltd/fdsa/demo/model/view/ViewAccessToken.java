package ltd.fdsa.demo.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "access_token")
public class ViewAccessToken {
	@ApiModelProperty(required=true, position= -10)
	@JsonProperty(value="access_token")
	private String accessToken;
	@ApiModelProperty(required=true, position= -1)
	@JsonProperty(value="refresh_token")
	private String refreshToken;
	@JsonProperty(value="expried_in")
	private Integer expriedIn;
}
