package ltd.fdsa.koffer.dir;

import ltd.fdsa.koffer.XEncryptor;
import ltd.fdsa.koffer.XEntryEncryptor;
import ltd.fdsa.koffer.XEntryFilter;
import ltd.fdsa.koffer.key.XKey;

import java.io.File;
import java.io.IOException;

/**
 * 文件夹加密器
 */
public class XDirEncryptor extends XEntryEncryptor<File> implements XEncryptor {

    public XDirEncryptor(XEncryptor xEncryptor) {
        this(xEncryptor, null);
    }

    public XDirEncryptor(XEncryptor xEncryptor, XEntryFilter<File> filter) {
        super(xEncryptor, filter);
    }

    @Override
    public void encrypt(XKey key, File src, File dest) throws IOException {
        if (src.isFile()) {
            XEncryptor encryptor = filtrate(src) ? xEncryptor : xNopEncryptor;
            encryptor.encrypt(key, src, dest);
        } else if (src.isDirectory()) {
            File[] files = src.listFiles();
            for (int i = 0; files != null && i < files.length; i++) {
                encrypt(key, files[i], new File(dest, files[i].getName()));
            }
        }
    }
}
