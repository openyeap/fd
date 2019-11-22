//package ltd.fdsa.boot.starter;
//
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
//import org.springframework.boot.context.event.*;
//import org.springframework.context.ApplicationEvent;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.context.event.*;
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
//public class ApplicationEventListener implements ApplicationListener<ApplicationEvent> {
//
//	@Override
//	public void onApplicationEvent(ApplicationEvent event) {
//		log.info(event.getClass().getName());
//		if (event instanceof ApplicationEnvironmentPreparedEvent) { // 初始化环境变量
//			log.info("初始化环境变量");
//		} else if (event instanceof ApplicationPreparedEvent) { // 初始化完成
//			log.info("初始化环境变量完成");
//		} else if (event instanceof ContextRefreshedEvent) { // 应用刷新，当ApplicationContext初始化或者刷新时触发该事件。
//			log.info("应用刷新");
//		} else if (event instanceof ApplicationReadyEvent) {// 应用已启动完成
//			log.info("应用已启动完成");
//		} else if (event instanceof ContextStartedEvent) { // 应用启动，Spring2.5新增的事件，当容器调用ConfigurableApplicationContext的
//															// Start()方法开始/重新开始容器时触发该事件。
//			log.info("应用启动");
//		} else if (event instanceof ContextStoppedEvent) { // 应用停止，Spring2.5新增的事件，当容器调用ConfigurableApplicationContext
//															// 的Stop()方法停止容器时触发该事件。
//			log.info("应用停止");
//		} else if (event instanceof ContextClosedEvent) { // 应用关闭，当ApplicationContext被关闭时触发该事件。容器被关闭时，其管理的所有 单例Bean都被销毁。
//			log.info("应用关闭");
//		} else {
//		}
//	}
//}