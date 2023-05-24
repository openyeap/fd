package cn.zhumingwu.starter.logger.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.db.DBAppender;
import ch.qos.logback.core.db.DriverManagerConnectionSource;
import ch.qos.logback.core.util.Duration;
import cn.zhumingwu.starter.logger.interceptor.LogInterceptor;
import cn.zhumingwu.starter.logger.properties.LoggingProperties;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import cn.zhumingwu.core.util.NamingUtils;
import net.logstash.logback.appender.LogstashTcpSocketAppender;
import net.logstash.logback.encoder.LogstashEncoder;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ImportAutoConfiguration({ LoggingProperties.class })
@Slf4j
public class LoggingAutoConfiguration implements WebMvcConfigurer, InitializingBean {

    private final LoggingProperties properties;

    public LoggingAutoConfiguration(LoggingProperties properties) {
        this.properties = properties;
    }

    @Value("${spring.application.name:Default}")
    private String name;

    @Override
    public void afterPropertiesSet() throws Exception {
        NamingUtils.formatLog(log, "LoggingFactory Start");
        if (this.properties.getLevel().size() <= 0) {
            this.properties.getLevel().put(Logger.ROOT_LOGGER_NAME, "ERROR");
        }
        for (var entry : this.properties.getLevel().entrySet()) {
            Logger logger = (Logger) LoggerFactory.getLogger(entry.getKey());
            var level = Level.toLevel(entry.getValue());

            LoggerContext context = logger.getLoggerContext();
            context.putProperty("name", this.name);
            var datasource = this.properties.getDatasource();
            if (datasource != null && Strings.isNotEmpty(datasource.getUrl()) && logger.getAppender("DB") == null) {
                NamingUtils.formatLog(log, "Logging Factory DB");
                var connSource = new DriverManagerConnectionSource();
                connSource.setDriverClass(datasource.getDriverClassName());
                connSource.setUrl(datasource.getUrl());
                connSource.setUser(datasource.getUsername());
                connSource.setPassword(datasource.getPassword());
                connSource.setContext(context);
                connSource.start();
                DBAppender dbAppender = new DBAppender();
                dbAppender.setConnectionSource(connSource);
                dbAppender.setContext(context);
                dbAppender.setName("DB");
                dbAppender.start();
                logger.addAppender(dbAppender);
            }
            var logstash = this.properties.getLogstash();
            if (logstash != null && Strings.isNotEmpty(logstash.getDestination())
                    && logger.getAppender("LOGSTASH") == null) {

                NamingUtils.formatLog(log, "Logging Factory Logstash:{}", entry);
                var encoder = new LogstashEncoder();
                encoder.setIncludeContext(false);
                encoder.setCustomFields("{\"name\":\"" + this.name + "\"}");
                encoder.setIncludeMdc(true);
                encoder.setEncoding("UTF-8");
                var appender = new LogstashTcpSocketAppender();
                appender.setReconnectionDelay(
                        Duration.buildBySeconds(logstash.getDuration() < 10 ? 10 : logstash.getDuration()));
                appender.addDestination(logstash.getDestination());
                appender.setEncoder(encoder);
                appender.setContext(context);
                appender.setName("LOGSTASH");
                appender.start();
                logger.addAppender(appender);

            }
        }
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        /**
         * 自定义拦截器，添加拦截路径和排除拦截路径 addPathPatterns():添加需要拦截的路径
         * excludePathPatterns():添加不需要拦截的路径
         * 在括号中还可以使用集合的形式，如注释部分代码所示
         */
        InterceptorRegistration registration = registry.addInterceptor(new LogInterceptor());
        registration.order(Integer.MIN_VALUE);
        var path = this.properties.getWebPath();
        if (path == null) {
            registration.addPathPatterns("/**");
            return;
        }

        if (path.getIncludePathPatterns() != null) {
            for (String item : path.getIncludePathPatterns()) {
                registration.addPathPatterns(item);
            }
        }
        if (path.getExcludePathPatterns() != null) {
            for (String item : path.getExcludePathPatterns()) {
                registration.excludePathPatterns(item);
            }
        }
    }
}
