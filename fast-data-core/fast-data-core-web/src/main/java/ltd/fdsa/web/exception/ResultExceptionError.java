package ltd.fdsa.web.exception;

import ltd.fdsa.web.enums.BusinessResult;


public class ResultExceptionError extends ResultException {

    /**
     * 统一异常处理：抛出默认失败信息
     */
    public ResultExceptionError() {
        super(BusinessResult.ERROR);
    }

    /**
     * 统一异常处理：抛出失败提示信息
     *
     * @param message 提示信息
     */
    public ResultExceptionError(String message) {
        super(BusinessResult.ERROR.getCode(), message);
    }
}
