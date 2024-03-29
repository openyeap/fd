package cn.zhumingwu.client.config;


import cn.zhumingwu.base.model.HttpCode;
import cn.zhumingwu.base.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


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

