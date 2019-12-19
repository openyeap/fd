package ltd.fdsa.fdsql.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecwid.consul.v1.ConsulClient;
 
@RestController("/")
public class Application {

	@Autowired
	ConsulClient consulClient;

	@RequestMapping("/home")
	public String home() {
		return "Hello World!";
	}

	@RequestMapping("/services")
	public Object services() {
		Object result = consulClient.getAgentServices().getValue();
		return result;
	}
}