package ltd.fdsa.boot.starter;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;
import com.ecwid.consul.v1.agent.model.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class BeforeStartup implements ApplicationListener<ApplicationStartedEvent> {

	@Autowired
	ConsulClient consulClient;

	@Autowired
	NewService service;

	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {
		log.info("==========ApplicationStartedEvent ServiceRegister start===========");
		try {
			consulClient.agentServiceRegister(service);
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage());
		}
		log.info("=========ApplicationStartedEvent ServiceRegister end===========");
	}

	 

}