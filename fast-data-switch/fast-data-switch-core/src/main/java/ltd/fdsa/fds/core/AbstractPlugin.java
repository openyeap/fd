package ltd.fdsa.fds.core;

public abstract class AbstractPlugin implements Pluginable {

	private String pluginName;

	private PluginType pluginType;
	@Override
	public String getPluginName() {
		return this.pluginName;
	}

	@Override
	public void setPluginName(String name) {
		this.pluginName = name;
	}

	@Override
	public PluginType getPluginType() {
		 
		return this.pluginType;
	}

	@Override
	public void setPluginType(PluginType type) {
		 this.pluginType = type;
	}
}
