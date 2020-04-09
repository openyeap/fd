//package ltd.fdsa.boot.starter;
//
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.ecwid.consul.v1.ConsulClient;
//import com.ecwid.consul.v1.agent.model.NewService;
//import com.ecwid.consul.v1.agent.model.Service;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Component
//@Slf4j
//public class Application implements ApplicationRunner {
//
//	@Autowired
//	ConsulClient consulClient;
//
//	@Autowired
//	NewService service;
//
//	@Override
//	public void run(ApplicationArguments args) throws Exception {
//		log.info("==========Application ServiceRegister start===========");
//		try {
//			consulClient.agentServiceRegister(service);
//		} catch (Exception ex) {
//			log.error(ex.getLocalizedMessage());
//		}
//		log.info("=========Application ServiceRegister end===========");
//	}
//
//} 