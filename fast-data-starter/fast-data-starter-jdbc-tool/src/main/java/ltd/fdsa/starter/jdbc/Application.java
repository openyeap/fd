package ltd.fdsa.starter.jdbc;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.starter.jdbc.config.JdbcProperty; 
import ltd.fdsa.starter.jdbc.service.CodeService;

/**
 * @author zhumingwu
 *
 */
@Slf4j
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);	
		app.run(args);
		
	}
	@Autowired
	protected JdbcProperty property;

	@Autowired
	private CodeService service;

	@PostConstruct
	public void test() {
		service.generate();
	}
}
