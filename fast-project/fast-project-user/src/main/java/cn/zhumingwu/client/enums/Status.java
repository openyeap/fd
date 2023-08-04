package cn.zhumingwu.client.enums;

public enum Status {
    Active(1, "active"),
    InActive(0, "inactive"),
    Deleted(-1, "deleted");
    private final int code;
    private final String name;

    Status(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}