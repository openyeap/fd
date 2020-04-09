package ltd.fdsa.cloud;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import reactor.core.publisher.Flux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.web.reactive.config.EnableWebFlux;

import lombok.extern.slf4j.Slf4j;
//@EnableDiscoveryClient
@Slf4j 
@EnableCircuitBreaker 
@SpringBootApplication 
public class Application {

	public static void main(String[] args) {
		try {
			log.debug("start");
			SpringApplication.run(Application.class, args);
			log.debug("end");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}
