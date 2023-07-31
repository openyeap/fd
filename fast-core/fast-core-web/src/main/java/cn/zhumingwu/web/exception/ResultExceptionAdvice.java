package cn.zhumingwu.web.exception;

import cn.zhumingwu.base.model.HttpCode;
import cn.zhumingwu.base.model.Result;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

// 异常通知器
@ControllerAdvice
public class ResultExceptionAdvice {
    // 运行切入程序集合
    private final List<ExceptionAdvice> proceed = new LinkedList<ExceptionAdvice>();

    // 添加切入程序
    public void putProceed(ExceptionAdvice advice) {
        proceed.add(advice);
    }

    // 拦截未知的运行时异常
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Result<Object> runtimeException(RuntimeException exception) {
        for (var advice : proceed) {
            advice.run(exception);
        }
        return Result.error(exception, HttpCode.INTERNAL_SERVER_ERROR.getCode());
    }

    // 拦截自定义异常
    @ExceptionHandler(ResultException.class)
    @ResponseBody
    public Result<Object> resultException(ResultException e) {
        return Result.fail(e.getCode(), e.getMessage(), null);
    }

    // 拦截表单验证异常
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public Result<Object> bindException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        var message = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
        return Result.fail(HttpCode.BAD_REQUEST, message);
    }
}
