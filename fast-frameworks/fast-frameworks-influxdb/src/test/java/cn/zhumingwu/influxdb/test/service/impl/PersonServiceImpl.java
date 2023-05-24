package cn.zhumingwu.influxdb.test.service.impl;

import cn.zhumingwu.influxdb.properties.InfluxProperties;
import cn.zhumingwu.influxdb.service.BaseInfluxService;
import com.influxdb.client.InfluxDBClient;
import lombok.var;
import cn.zhumingwu.influxdb.test.model.Person;
import cn.zhumingwu.influxdb.test.service.PersonService;
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
