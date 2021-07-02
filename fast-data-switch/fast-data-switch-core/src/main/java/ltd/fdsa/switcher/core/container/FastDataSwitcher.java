package ltd.fdsa.switcher.core.container;

import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.core.util.PluginManager;

@Slf4j
public class FastDataSwitcher {
    // step 1 初始化引擎,扫描配置文件中的插件
    static final PluginManager pluginManager = PluginManager.getDefaultInstance();

    static {

        pluginManager.scan("./plugins");
    }

    public static PluginManager getPluginManager() {
        return pluginManager;
    }
}
