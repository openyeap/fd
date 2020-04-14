package ltd.fdsa.demo.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "refresh_token")
public class ViewRefreshToken {
	@ApiModelProperty(required=true, position= -10)
	@JsonProperty(value="access_token")
	private String accessToken;
	@ApiModelProperty(required=true, position= -1)
	@JsonProperty(value="refresh_token")
	private String refreshToken;
 
}
