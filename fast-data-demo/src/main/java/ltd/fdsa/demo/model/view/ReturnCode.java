package ltd.fdsa.demo.model.view;

public class ReturnCode {

	public static class NAME_IS_NULL {
		public static final int value = 1001;
		public static final String desc = "用户名不得为空！";
	}

	public static class PASSWORD_IS_NULL {
		public static final int value = 1002;
		public static final String desc = "密码不得为空！";
	}

	public static class LOGIN_FAILED {
		public static final int value = 1003;
		public static final String desc = "用户登录失败，请重新再试！";
	}

	public static class TOKEN_IS_MISSED {
		public static final int value = 1004;
		public static final String desc = "刷新令牌不得为空！";
	}

	public static class PARAMETER_IS_WRONG {
		public static final int value = 1009;
		public static final String desc = "输入参数不正确！";
	}

	public static class UPLOAD_FILE_FAILED {
		public static final int value = 1010;
		public static final String desc = "上传文件失败！";
	}
}
