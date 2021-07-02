package ltd.fdsa.koffer.boot;

import ltd.fdsa.koffer.XLauncher;
import org.springframework.boot.loader.WarLauncher;

import java.net.URL;

/**
 * Spring-Boot Jar 启动器
 */
public class XWarLauncher extends WarLauncher {
    private final XLauncher xLauncher;

    public XWarLauncher(String... args) throws Exception {
        this.xLauncher = new XLauncher(args);
    }

    public static void main(String[] args) throws Exception {
        new XWarLauncher(args).launch();
    }

    public void launch() throws Exception {
        launch(xLauncher.args);
    }

    @Override
    protected ClassLoader createClassLoader(URL[] urls) throws Exception {
        return new XBootClassLoader(
                urls,
                this.getClass().getClassLoader(),
                xLauncher.xDecryptor,
                xLauncher.xEncryptor,
                xLauncher.xKey);
    }
}
