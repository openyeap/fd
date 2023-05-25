package cn.zhumingwu.base.support;

interface CodeBasedEnum {
    int getCode();
}

interface SelfDescribedEnum {
    String name();
    default String getName() {
        return name();
    }
    default String getDescription() {
        return name();
    }
}

public interface CommonEnum extends CodeBasedEnum, SelfDescribedEnum {

}