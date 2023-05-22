package ltd.fdsa.core.support;

interface CodeBasedEnum {
    int getCode();
}

interface SelfDescribedEnum {
    default String getName() {
        return name();
    }

    String name();

    String getDescription();
}

public interface CommonEnum extends CodeBasedEnum, SelfDescribedEnum {

}