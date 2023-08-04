package cn.zhumingwu.client.enums;


public enum PhoneType {
    Mobile(0, "mobile phone"),
    Telephone(1, "telephone");

    private final int code;
    PhoneType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return this.code;
    }

    private final String name;

    public String getName() {
        return this.name;
    }
}

