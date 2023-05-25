package cn.zhumingwu.dataswitch.core.container;

public enum PluginType {
    PIPELINE(0, "pipeline"),
    SOURCE(1, "source"),
    CHANNEL(2, "channel"),
    TARGET(3, "target");

    private final int value;
    private final String name;

    PluginType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }
}
