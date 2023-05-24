package cn.zhumingwu.database.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Map;
import java.util.Random;

@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {

    private int readSize;

    public DynamicDataSource(Map<Object, Object> datasources) {
        this.readSize = datasources.size();
        super.setTargetDataSources(datasources);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        if (readSize == 0) {
            return 0;
        }
        int result = new Random().nextInt(readSize);
        return result;
    }
}
