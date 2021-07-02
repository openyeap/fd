package ltd.fdsa.cloud.exception;


import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.web.enums.HttpCode;
import ltd.fdsa.web.view.Result;
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
        var data = Result.fail(HttpCode.INTERNAL_SERVER_ERROR);
        return data;
    }
}
