package ltd.fdsa.database.entity;

public enum Status {

    /**
     * 正常状态码
     */

    OK((byte) 1, "正常"),
    /**
     * 冻结状态码
     */
    FREEZED((byte) 2, "冻结"),

    /**
     * 删除状态码
     */
    DELETE((byte) 3, "删除");

    private Byte code;

    private String message;

    Status(Byte code, String message) {
        this.code = code;
        this.message = message;
    }
}
