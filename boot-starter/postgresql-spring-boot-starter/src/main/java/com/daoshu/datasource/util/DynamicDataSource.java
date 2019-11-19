package com.daoshu.datasource.util;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.daoshu.datasource.constant.DataSourceKey;


public class DynamicDataSource extends AbstractRoutingDataSource {

    private Map<Object, Object> datasources;

    public DynamicDataSource() {
        datasources = new HashMap<>();

        super.setTargetDataSources(datasources);

    }

    public <T extends DataSource> void addDataSource(DataSourceKey key, T data) {
        datasources.put(key, data);
    }
    public <T extends DataSource> void addDataSource(String key, T data) {
        datasources.put(key, data);
    }
    protected Object determineCurrentLookupKey() {
        return DataSourceHolder.getDataSourceKey();
    }

}