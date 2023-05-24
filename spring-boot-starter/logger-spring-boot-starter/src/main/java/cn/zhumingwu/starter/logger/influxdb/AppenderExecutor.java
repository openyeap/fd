package cn.zhumingwu.starter.logger.influxdb;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Context;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;

/**
 * Converts a log event into a compatible object for api influxdb
 */
public class AppenderExecutor {

    private final InfluxDbConverter influxDbConverter;

    private final InfluxDB influxdb;
    private final Context context;
    private final InfluxDbSource source;

    public AppenderExecutor(InfluxDbConverter influxDbConverter, InfluxDbSource source,
                            InfluxDB influxDB, Context context) {
        this.influxDbConverter = influxDbConverter;
        this.source = source;
        this.influxdb = influxDB;
        this.context = context;
    }

    /**
     * The main append method. Takes the event that is being logged, formats if for InfluxDB and then sends it over the wire
     * to the metrics endpoints
     *
     * @param logEvent The event that we are logging
     */
    public void append(final ILoggingEvent logEvent) {
        String loggerName = logEvent.getLoggerName();

        Point point = influxDbConverter.toInflux(logEvent, this.source, this.context);

        influxdb.write(point);
    }

}
