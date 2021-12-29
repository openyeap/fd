package ltd.fdsa.web.view;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ltd.fdsa.web.enums.BusinessResult;
import ltd.fdsa.web.enums.HttpCode;
import ltd.fdsa.web.enums.ResultCode;

/**
 * 响应数据(结果)最外层对象
 *
 * 
 */
@Data
@ApiModel("响应结果")
public class Result<T> {
    /**
     * 状态码
     */
    @ApiModelProperty(notes = "状态码（200成功、400错误）")
    private int code;
    /**
     * 提示信息
     */
    @ApiModelProperty(notes = "提示信息")
    private String message;

    /**
     * 响应数据
     */
    @ApiModelProperty(notes = "响应数据")
    private T data;

    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        return new Result<T>(HttpCode.OK.getCode(), HttpCode.OK.getMessage(), data);
    }

    public static <T> Result<T> success() {
        return new Result<T>(HttpCode.OK.getCode(), HttpCode.OK.getMessage(), null);
    }

    public static <T> Result<T> fail(int code, String message) {

        return new Result<T>(code, message, null);
    }

    public static <T> Result<T> fail(int code, String message, T data) {
        return new Result<T>(code, message, data);
    }

    public static <T> Result<T> fail(HttpCode code) {
        return new Result<T>(code.getCode(), code.getMessage(), null);
    }

    public static <T> Result<T> fail(HttpCode code, T data) {
        return new Result<T>(code.getCode(), code.getMessage(), data);
    }

    public static <T> Result<T> fail(BusinessResult code) {
        return new Result<T>(code.getCode(), code.getMessage(), null);
    }

    public static <T> Result<T> fail(BusinessResult code, T data) {
        return new Result<T>(code.getCode(), code.getMessage(), data);
    }

    public static <T> Result<T> fail(ResultCode code) {
        return new Result<T>(code.getCode(), code.getMessage(), null);
    }

    public static <T> Result<T> fail(ResultCode code, T data) {
        return new Result<T>(code.getCode(), code.getMessage(), data);
    }

    public static <T> Result<T> error(Exception ex) {
        return new Result<T>(HttpCode.INTERNAL_SERVER_ERROR.getCode(), ex.getMessage(), null);
    }

    public static <T> Result<T> error(Exception ex, int code) {
        return new Result<T>(code, ex.getMessage(), null);
    }
}
