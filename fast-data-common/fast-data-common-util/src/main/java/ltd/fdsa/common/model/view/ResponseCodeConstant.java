package ltd.fdsa.common.model.view;


public class ResponseCodeConstant {

    public static class SUCCESS {
        public static final int value = 200;
        public static final String desc = "成功";
    }

    public static class BROKE {
        public static final int value = 9999;
        public static final String desc = "已熔断";
    }

    public static class NOT_FOUND {
        public static final int value = 404;
        public static final String desc = "请求的服务不存在";
    }

    public static class BUSINESS_ERROR {
        public static final int value = 10001;
        public static final String desc = "业务逻辑异常";
    }
 
    public static class PARAMETER_EMPTY {
        public static final int value = 10003;
        public static final String desc = "参数不能为空";
    }

    public static class PARAMETER_INCORRECT {
        public static final int value = 10004;
        public static final String desc = "参数错误";
    }
 
    public static class UPLOAD_ERROR {
        public static final int value = 1002;
        public static final String desc = "文件上传异常";
    }

    public static class NULL_POINT_ERROR {
        public static final int value = 2001;
        public static final String desc = "空指针异常";
    }

    public static class SQL_ERROR {
        public static final int value = 2002;
        public static final String desc = "SQL执行异常";
    }

    public static class FILE_NOT_FOUND_ERROR {
        public static final int value = 2003;
        public static final String desc = "文件不存在";
    }

    public static class OTHER_ERROR {
        public static final int value = 9001;
        public static final String desc = "其他异常";
    }
 

}
