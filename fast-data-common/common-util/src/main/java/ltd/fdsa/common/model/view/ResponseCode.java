package ltd.fdsa.common.model.view;



public enum ResponseCode {
/*
 * 成功
 * */
    SUCCESS(ResponseCodeConstant.SUCCESS.value, ResponseCodeConstant.SUCCESS.desc),
    BROKE(ResponseCodeConstant.BROKE.value, ResponseCodeConstant.BROKE.desc),
    NOT_FOUND(ResponseCodeConstant.NOT_FOUND.value, ResponseCodeConstant.NOT_FOUND.desc),

    BUSINESS_ERROR(ResponseCodeConstant.BUSINESS_ERROR.value, ResponseCodeConstant.BUSINESS_ERROR.desc),

    // Common
    PARAMETER_EMPTY(ResponseCodeConstant.PARAMETER_EMPTY.value, ResponseCodeConstant.PARAMETER_EMPTY.desc),
    PARAMETER_INCORRECT(ResponseCodeConstant.PARAMETER_INCORRECT.value, ResponseCodeConstant.PARAMETER_INCORRECT.desc),
  
    UPLOAD_ERROR(ResponseCodeConstant.UPLOAD_ERROR.value, ResponseCodeConstant.UPLOAD_ERROR.desc),
    NULL_POINT_ERROR(ResponseCodeConstant.NULL_POINT_ERROR.value, ResponseCodeConstant.NULL_POINT_ERROR.desc),
    SQL_ERROR(ResponseCodeConstant.SQL_ERROR.value, ResponseCodeConstant.SQL_ERROR.desc),
    FILE_NOT_FOUND_ERROR(ResponseCodeConstant.FILE_NOT_FOUND_ERROR.value, ResponseCodeConstant.FILE_NOT_FOUND_ERROR.desc),
    OTHER_ERROR(ResponseCodeConstant.OTHER_ERROR.value, ResponseCodeConstant.OTHER_ERROR.desc);
 
    private final int value;
    private String description;

  ResponseCode(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
