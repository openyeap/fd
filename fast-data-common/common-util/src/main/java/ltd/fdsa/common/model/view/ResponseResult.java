package ltd.fdsa.common.model.view;

import lombok.Data;

@Data 
public class ResponseResult<T> {
	private int code;
	private String message;
	private T data;

	private ResponseResult(int code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	private ResponseResult(ResponseCode returnStatus, T data) {
		this.code = returnStatus.getValue();
		this.message = returnStatus.getDescription();
		this.data = data;
	}

	private ResponseResult(ResponseCode returnStatus) {
		this.code = returnStatus.getValue();
		this.message = returnStatus.getDescription();
	}

	public static <T> ResponseResult<T> exception(Exception ex) {
		ResponseResult<T> response = new ResponseResult<T>(ResponseCode.OTHER_ERROR.getValue(), ex.getMessage(), null);
		return response;
	} 
	public static <T> ResponseResult<T> success(T data) {
		return new ResponseResult<T>(ResponseCode.SUCCESS, data);
	}

	public static <T> ResponseResult<T> success() {
		return new ResponseResult<T>(ResponseCode.SUCCESS);
	}
	public static <T> ResponseResult<T> fail(int code , String message, T data) {
		return new ResponseResult<T>(code,message, data);
	}

	public static ResponseResult<String> fail(ResponseCode status) {
		ResponseResult<String> response = new ResponseResult<String>(status);
		return response;
	}
}

