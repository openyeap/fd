package ltd.fdsa.fdsql.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecwid.consul.v1.ConsulClient;
 
@RestController("/v2")
public class HomeController {

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
	
	@RequestMapping("/api-docs")
	public Object getApiDocs() {
		// TODO get all tables and views from jdbc source
		// refer to https://blog.csdn.net/smily_tk/article/details/82663106
		// then generate api-docs for swagger ui
		Object result = null;//
		return result;
	} 
}