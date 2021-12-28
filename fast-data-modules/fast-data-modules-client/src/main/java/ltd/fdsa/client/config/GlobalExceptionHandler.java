package ltd.fdsa.client.config;


import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.database.config.DataSourceConfig;
import ltd.fdsa.web.enums.HttpCode;
import ltd.fdsa.web.view.Result;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result<Object> goalException(Exception ex) {
        log.error("系统异常", ex);
        return Result.fail(HttpCode.EXPECTATION_FAILED);
    }
}

