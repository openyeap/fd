package ltd.fdsa.fdsql.web.controller;

import ltd.fdsa.fdsql.web.config.ChangesFilterConfig;
import ltd.fdsa.fdsql.web.config.TablesFilterConfig;
import ltd.fdsa.fdsql.web.repository.JdbcService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ecwid.consul.v1.ConsulClient;

@RestController
@RequestMapping("/v2")
public class HomeController {

    @Autowired
    ConsulClient consulClient;

    @Autowired
    JdbcService jdbcService;

    @Autowired
    TablesFilterConfig tablesFilterConfig;

    @Autowired
    ChangesFilterConfig changesFilterConfig;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home() {
        return tablesFilterConfig.toString();
    }

    @RequestMapping("/services")
    public Object services() {
        Object result = consulClient.getAgentServices().getValue();
        return result;
    }

    @RequestMapping("/api-docs")
    public Object getApiDocs() {
        Object result = jdbcService.createTablesController("localhost:9080/mti-service-job", "");
        return result;
    }

    @RequestMapping(value = "/tables", method = RequestMethod.GET, produces = "application/json")
    public Object getTables() {
        Object obj = tablesFilterConfig.queryTables();
        return obj;
    }

    @RequestMapping(value = "/tables1", method = RequestMethod.GET, produces = "application/json")
    public Object getChangeTables() {
        Object obj = changesFilterConfig.queryTables();
        return obj;
    }

    @RequestMapping(value = "/tables2", method = RequestMethod.GET, produces = "application/json")
    public Object getChangeTablesDZ() {
        Object obj = changesFilterConfig.getTablesChangeList();
        return obj;
    }

    @RequestMapping(value = "/column/{name}", method = RequestMethod.GET, produces = "application/json")
    public Object getColumns(@PathVariable String name) {
        Object obj = changesFilterConfig.getColumnsList(name);
        return obj;
    }
}