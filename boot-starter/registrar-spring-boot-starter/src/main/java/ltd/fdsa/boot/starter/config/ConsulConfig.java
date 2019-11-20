package ltd.fdsa.boot.starter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ecwid.consul.transport.TLSConfig;
import com.ecwid.consul.transport.TLSConfig.KeyStoreInstanceType;
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

	@Bean(name = "readDataSource")
	//@ConfigurationProperties("spring.cloud.consul.host.port")  
    public ConsulClient createConsulClient() {
	
		 //TODO TLSConfig tlsConfig = new TLSConfig();
        return  new ConsulClient(this.agentHost, this.agentPort);
    }
 

}
