package cn.zhumingwu.cloud.constant;

public enum MockHandleTypeEnum {

    DEFAULT(0, "默认"),
    RANDOM(1, "随机"),
    ROLL(2, "轮询");

    private Integer code;

    private String value;

    private MockHandleTypeEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static String getByCode(Integer code) {
        for (MockHandleTypeEnum mockHandleTypeEnum : MockHandleTypeEnum.values()) {
            if (mockHandleTypeEnum.code == code) {
                return mockHandleTypeEnum.getValue();
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}