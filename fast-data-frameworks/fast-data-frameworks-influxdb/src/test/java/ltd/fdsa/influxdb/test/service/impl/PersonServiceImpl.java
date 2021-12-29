package ltd.fdsa.influxdb.test.service.impl;

import com.influxdb.client.InfluxDBClient;
import lombok.var;
import ltd.fdsa.influxdb.properties.InfluxProperties;
import ltd.fdsa.influxdb.service.BaseInfluxService;
import ltd.fdsa.influxdb.test.model.Person;
import ltd.fdsa.influxdb.test.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl extends BaseInfluxService<Person> implements PersonService {

    static ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();

    static {
        var list = Arrays.stream("cache1".split("abc")).collect(Collectors.toList());
        cacheManager.setCacheNames(list);
        System.out.println("process");
    }

    @Autowired
    public PersonServiceImpl(InfluxDBClient influxDBClient, InfluxProperties properties) {
        super(influxDBClient, properties, cacheManager.getCache("cache1"));

    }

}
