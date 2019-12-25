package ltd.fdsa.cloud.config;

import ltd.fdsa.cloud.util.ConsulServerUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.ecwid.consul.v1.ConsulClient;

@Configuration 
public class ConsulConfig {
	@Value("${spring.cloud.consul.host:localhost}")
	String agentHost;
	@Value("${spring.cloud.consul.port:8500}")
	int agentPort;
//	@Value("${spring.cloud.consul.tls.type:8500}")
//	KeyStoreInstanceType keyStoreInstanceType;
//	@Value("${spring.cloud.consul.tls.certificatePath}")
//	String certificatePath;
//	@Value("${spring.cloud.consul.tls.certificatePassword}")
//	String certificatePassword;
//	@Value("${spring.cloud.consul.tls.keyStorePath}")
//	String keyStorePath;
//	@Value("${spring.cloud.consul.tls.keyStorePassword}")
//	 String keyStorePassword;
//		 

	@Bean(name = "consulClient")
	//@ConfigurationProperties("spring.cloud.consul.host.port")  
    public ConsulClient createConsulClient() {
		 //TODO TLSConfig tlsConfig = new TLSConfig();
		ConsulClient consulClient = new ConsulClient(this.agentHost, this.agentPort);
		ConsulServerUtil.getInstance().init(consulClient);
        return consulClient;
    }
}
