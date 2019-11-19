package com.daoshu.cloud;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class Application {

	public static void main(String[] args) {
		try {
			log.debug("start");
			Flux.interval(Duration.of(1, ChronoUnit.SECONDS)).subscribe(l -> {
				log.debug("starts");
				log.debug(l.toString());
			});
			
			log.debug("end");
			SpringApplication.run(Application.class, args);

		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
		}
	}
}
