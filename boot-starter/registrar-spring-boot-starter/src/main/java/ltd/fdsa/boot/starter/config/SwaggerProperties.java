package ltd.fdsa.boot.starter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Data;


@ConfigurationProperties("swagger")
@Data
public class SwaggerProperties {
	private String title;
	private String name;
	private String email;
	private String description;
	private String version;
	private String urlTerm;
	private String urlContact;
	private String packageName;
}