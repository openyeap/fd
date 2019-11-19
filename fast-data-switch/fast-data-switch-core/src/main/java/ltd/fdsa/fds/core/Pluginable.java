package ltd.fdsa.fds.core;

public interface Pluginable {

	public String getPluginName();

	public void setPluginName(String name);
	
	public PluginType getPluginType();
	
	public void setPluginType(PluginType type);
}
