package cn.zhumingwu.client.enums;

public enum AllowUserType {
    All(0, "所有用户"),
    StaffOnly(1, "所有员工"),
    GroupOnly(2, "指定小组"),
    DepartmentOnly(4, "指定部门"),
    RolesOnly(8, "指定角色"),
    LeaderOnly(16, "直接领导"),
    DepartmentLeaderOnly(32, "部门领导"),
    CreatorSpecify(64, "发起人指定"),
    Creator(128, "发起人指定");

    AllowUserType(Integer code, String name) {
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
