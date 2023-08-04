package cn.zhumingwu.client.enums;

public enum UserType {
    User(0, "User"),
    Support(1, "Support"),
    Admin(100, "Admin");

    UserType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    private final Integer code;

    public Integer getCode() {
        return this.code;
    }

    private final String name;

    public String getName() {
        return this.name;
    }
}