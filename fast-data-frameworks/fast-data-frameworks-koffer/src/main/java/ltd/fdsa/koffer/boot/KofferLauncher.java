package ltd.fdsa.koffer.boot;

import ltd.fdsa.koffer.XLauncher;
import org.springframework.boot.loader.JarLauncher;

import java.net.URL;

/**
 * Spring-Boot Jar 启动器
 */
public class KofferLauncher extends JarLauncher {
    private final XLauncher xLauncher;

    public KofferLauncher(String... args) throws Exception {
        this.xLauncher = new XLauncher(args);
    }

    public static void main(String[] args) throws Exception {
        new KofferLauncher(args).launch();
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
