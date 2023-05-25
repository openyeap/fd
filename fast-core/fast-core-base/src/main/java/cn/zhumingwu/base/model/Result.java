package cn.zhumingwu.base.model;

import lombok.Data;

/**
 * 响应数据(结果)最外层对象
 *
 */
@Data
public class Result<T> {
    public final static int OK = 200;
    /**
     * 状态码
     */
//    @ApiModelProperty(notes = "状态码（200成功、400错误）")
    private int code;
    /**
     * 提示信息
     */
//    @ApiModelProperty(notes = "提示信息")
    private String message;

    /**
     * 响应数据
     */
//    @ApiModelProperty(notes = "响应数据")
    private T data;

    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }


    public static <T> Result<T> success(T... data) {
        if (data.length > 0) {
            return new Result<T>(HttpCode.OK.getCode(), HttpCode.OK.getMessage(), data[0]);
        }
        return new Result<T>(HttpCode.OK.getCode(), HttpCode.OK.getMessage(), null);
    }

    public static <T> Result<T> fail(int code, String message, T... data) {
        if (data.length > 0) {
            return new Result<T>(code, message, data[0]);

        }
        return new Result<T>(code, message, null);
    }

    public static <T> Result<T> fail(HttpCode code, T... data) {
        if (data.length > 0) {
            return new Result<T>(code.getCode(), code.getMessage(), data[0]);
        }
        return new Result<T>(code.getCode(), code.getMessage(), null);
    }

    public static <T> Result<T> fail(BusinessResult code, T... data) {
        if (data.length > 0) {
            return new Result<T>(code.getCode(), code.getMessage(), data[0]);
        }
        return new Result<T>(code.getCode(), code.getMessage(), null);
    }

    public static <T> Result<T> fail(ResultCode code, T... data) {
        if (data.length > 0) {
            return new Result<T>(code.getCode(), code.getMessage(), data[0]);
        }
        return new Result<T>(code.getCode(), code.getMessage(), null);
    }

    public static <T> Result<T> error(Exception ex, int... code) {
        if (code.length > 0) {
            return new Result<T>(code[0], ex.getMessage(), null);
        }
        return new Result<T>(HttpCode.INTERNAL_SERVER_ERROR.getCode(), ex.getMessage(), null);
    }
}
