package cn.zhumingwu.starter.logger.influxdb;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Context;
import org.influxdb.dto.Point;

/**
 * Created by nicolas on 15/03/15.
 */
public class InfluxDbConverter {
    //
    public Point toInflux(ILoggingEvent loggingEvent, InfluxDbSource source, Context context) {
        Point series = Point.measurement("java")
                .addField("level", loggingEvent.getLevel().toString())
                .addField("logger", loggingEvent.getLoggerName())
                .addField("message", loggingEvent.getMessage())
                .addField("thread", loggingEvent.getThreadName())
                .addField("timestamp", loggingEvent.getTimeStamp())
                .build();
        return series;
    }
}
