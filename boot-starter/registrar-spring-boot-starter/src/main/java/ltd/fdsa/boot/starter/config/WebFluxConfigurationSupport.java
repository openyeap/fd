package ltd.fdsa.boot.starter.config; 
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.cors.CorsConfiguration;

import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.boot.starter.Application;
@Slf4j
public class WebFluxConfigurationSupport implements ApplicationContextAware {

 
	@Nullable
	private ApplicationContext applicationContext;
 
	@Override
	public void setApplicationContext(@Nullable ApplicationContext applicationContext) {
		
		log.debug("test");
		
		this.applicationContext = applicationContext;
		if (applicationContext != null) {
				Assert.state(!applicationContext.containsBean("mvcContentNegotiationManager"),
						"The Java/XML config for Spring MVC and Spring WebFlux cannot both be enabled, " +
						"e.g. via @EnableWebMvc and @EnableWebFlux, in the same application.");
		}
		
		log.debug("test");
	}

	@Nullable
	public final ApplicationContext getApplicationContext() {
		return this.applicationContext;
	}}
 