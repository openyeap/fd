//package ltd.fdsa.boot.starter;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.boot.context.event.ApplicationStartedEvent;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.annotation.Configuration;
//
//import com.ecwid.consul.v1.ConsulClient;
//import com.ecwid.consul.v1.agent.model.NewService;
//
//import lombok.extern.slf4j.Slf4j;
//import ltd.fdsa.boot.starter.config.ConsulAutoConfiguration;
//
//@Slf4j
//@ConditionalOnClass(ConsulAutoConfiguration.class)
////@EnableConfigurationProperties(StarterServiceProperties.class)
//@Configuration
//public class BeforeStartup implements ApplicationListener<ApplicationStartedEvent> {
//
//	@Autowired
//	ConsulClient consulClient;
//
//	@Autowired
//	NewService service;
//
//	@Override
//	public void onApplicationEvent(ApplicationStartedEvent event) {
//		log.info("==========ApplicationStartedEvent ServiceRegister start===========");
//		try {
//			consulClient.agentServiceRegister(service);
//		} catch (Exception ex) {
//			log.error(ex.getLocalizedMessage());
//		}
//		log.info("=========ApplicationStartedEvent ServiceRegister end===========");
//	}
//
//}