package cn.zhumingwu.starter.logger.influxdb;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import lombok.Data;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;

@Data
public class InfluxDbAppender extends AppenderBase<ILoggingEvent> {

    private AppenderExecutor appenderExecutor;
    private InfluxDbSource source;

    @Override
    protected void append(ILoggingEvent iLoggingEvent) {
        appenderExecutor.append(iLoggingEvent);
    }

    @Override
    public void start() {
        initExecutor();
        super.start();

    }

    /**
     * This is an ad-hoc dependency injection mechanism. We don't want create all
     * these classes every time a message is
     * logged. They will hang around for the lifetime of the appender.
     */
    private void initExecutor() {
        InfluxDB influxdb = InfluxDBFactory.connect("http://" + source.getIp() + ":" + source.getPort(),
                source.getUser(), source.getPassword());
        String database = this.source.getDatabase();
        var q = influxdb.query(new Query("CREATE DATABASES " + database));
        influxdb.setDatabase(database);
        InfluxDbConverter converter = new InfluxDbConverter();
        appenderExecutor = new AppenderExecutor(converter, source, influxdb, getContext());
    }
}
