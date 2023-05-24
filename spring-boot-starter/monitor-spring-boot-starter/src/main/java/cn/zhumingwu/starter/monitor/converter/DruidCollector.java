package cn.zhumingwu.starter.monitor.converter;

import com.alibaba.druid.pool.DruidDataSource;
import io.prometheus.client.Collector;
import io.prometheus.client.GaugeMetricFamily;
import lombok.var;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class DruidCollector extends Collector {

    private static final List<String> LABEL_NAMES = Collections.singletonList("pool");

    private final Map<String, DruidDataSource> dataSources;

    public DruidCollector(Map<String, DruidDataSource> dataSources) {
        this.dataSources = dataSources;
    }

    @Override
    public List<MetricFamilySamples> collect() {

        this.dataSources.forEach((k, v) -> {
            var stat = v.getDataSourceStat();

        });
        return Arrays.asList(
                createGauge("druid_active_count", "Active count",
                        druidDataSource -> (double) druidDataSource.getActiveCount()),
                createGauge("druid_active_peak", "Active peak",
                        druidDataSource -> (double) druidDataSource.getActivePeak()),
                createGauge("druid_error_count", "Error count",
                        druidDataSource -> (double) druidDataSource.getErrorCount()),
                createGauge("druid_execute_count", "Execute count",
                        druidDataSource -> (double) druidDataSource.getExecuteCount()),
                createGauge("druid_max_active", "Max active",
                        druidDataSource -> (double) druidDataSource.getMaxActive()),
                createGauge("druid_min_idle", "Min idle",
                        druidDataSource -> (double) druidDataSource.getMinIdle()),
                createGauge("druid_max_wait", "Max wait",
                        druidDataSource -> (double) druidDataSource.getMaxWait()),
                createGauge("druid_max_wait_thread_count", "Max wait thread count",
                        druidDataSource -> (double) druidDataSource.getMaxWaitThreadCount()),
                createGauge("druid_pooling_count", "Pooling count",
                        druidDataSource -> (double) druidDataSource.getPoolingCount()),
                createGauge("druid_pooling_peak", "Pooling peak",
                        druidDataSource -> (double) druidDataSource.getPoolingPeak()),
                createGauge("druid_rollback_count", "Rollback count",
                        druidDataSource -> (double) druidDataSource.getRollbackCount()),
                createGauge("druid_wait_thread_count", "Wait thread count",
                        druidDataSource -> (double) druidDataSource.getWaitThreadCount())
        );
    }

    private GaugeMetricFamily createGauge(String metric, String help,
                                          Function<DruidDataSource, Double> metricValueFunction) {
        GaugeMetricFamily metricFamily = new GaugeMetricFamily(metric, help, LABEL_NAMES);
        dataSources.forEach((s, druidDataSouce) -> metricFamily.addMetric(
                Collections.singletonList(s),
                metricValueFunction.apply(druidDataSouce)
        ));
        return metricFamily;
    }


}
