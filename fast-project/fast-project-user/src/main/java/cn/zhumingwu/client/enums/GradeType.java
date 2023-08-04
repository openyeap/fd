package cn.zhumingwu.client.enums;

public enum GradeType {
    Grade1(1, "1"),
    Grade2(2, "2"),
    Grade3(3, "3"),
    Grade4(4, "4"),
    Grade5(5, "5");
    GradeType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    private final int code;


    public Integer getCode() {
        return this.code;
    }

    private final String name;

    public String getName() {
        return this.name;
    }
}