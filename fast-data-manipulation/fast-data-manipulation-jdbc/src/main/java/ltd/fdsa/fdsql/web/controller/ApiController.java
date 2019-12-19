package ltd.fdsa.fdsql.web.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ltd.fdsa.fdsql.parser.RdmlParser;
import ltd.fdsa.fdsql.web.repository.Repository;
 
@RestController("/")
public class ApiController {
	@Autowired
	private Repository repository;
	@RequestMapping(value = "/{table}", method = RequestMethod.PUT, produces = "application/json")
	public String create(@PathVariable String table, @RequestBody Map<String, Object> data) {
		
		// TODO insert data into table;
		return "Hello World!";
	}

	@RequestMapping(value = "/{table}/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public String delete(@PathVariable String table, @RequestParam(defaultValue = "") String where,
			@PathVariable String id) {
		
		// TODO delete data from table;
		return "Hello World!";
	}

	@RequestMapping(value = "/{table}/{id}", method = RequestMethod.POST, produces = "application/json")
	public String update(@PathVariable String table, @RequestParam(defaultValue = "") String where,
			@PathVariable String id) {
		// TODO update data in table;
		return "Hello World!";
	}

	@RequestMapping(value = "/{table}", method = RequestMethod.GET, produces = "application/json")
	public Object query(@PathVariable String table, @RequestParam(defaultValue = "*") String select,
			@RequestParam(defaultValue = "") String query, @RequestParam(defaultValue = "") String order,
			@RequestParam(defaultValue = "") String group, @RequestParam(defaultValue = "") String having,
			@RequestParam(required = false, defaultValue = "0") int page,
			@RequestParam(required = false, defaultValue = "20") int size) {
			
		StringBuffer sql = new StringBuffer();
		RdmlParser parser= new RdmlParser();
		sql.append(parser.parseSelect(select));
		sql.append("\r\n");
		sql.append("from ");
		sql.append(table);
		sql.append("\r\n");
		sql.append(parser.parseQuery(query));
		sql.append("\r\n");
		sql.append(parser.parseOrder(order));
		sql.append("\r\n");
		sql.append(parser.parsePage(page, size));
		return repository.query(sql.toString());
	}
}