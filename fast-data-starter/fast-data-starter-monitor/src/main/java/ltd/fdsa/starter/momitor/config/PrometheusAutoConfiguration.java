package ltd.fdsa.starter.momitor.config;
 
import ltd.fdsa.starter.momitor.PrometheusActuatorEndpoint;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication
public class PrometheusAutoConfiguration {
  
	@Bean
    @ConditionalOnMissingBean
    @ConditionalOnAvailableEndpoint
    public PrometheusActuatorEndpoint customEndpoint(){
        return new PrometheusActuatorEndpoint();
    }
}