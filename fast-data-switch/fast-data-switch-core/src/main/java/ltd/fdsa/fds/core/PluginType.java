package ltd.fdsa.fds.core;

 
public enum PluginType {

    READER("ds"),  WRITER("dt"), PIPELINE("pl");

    private String pluginType;

    private PluginType(String pluginType) {
        this.pluginType = pluginType;
    }

    @Override
    public String toString() {
        return this.pluginType;
    }
}
