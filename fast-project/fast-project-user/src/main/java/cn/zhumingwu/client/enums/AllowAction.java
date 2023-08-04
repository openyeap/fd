package cn.zhumingwu.client.enums;

public enum AllowAction {
    Query(0B00000001, "查询"),
    Write(0B00000011, "修改"),
    Submit(0B00000111, "提交"),
    SubmitTo(0B00001111, "提交到"),
    Cancel(0B10000001, "撤消"),
    Reject(0B11000001, "拒绝"),
    Approve(0B00010001, "同意"),
    Transfer(0B00110001, "转移"),
    ReturnTo(0B11010001, "回退到");

    AllowAction(Integer code, String name) {
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
