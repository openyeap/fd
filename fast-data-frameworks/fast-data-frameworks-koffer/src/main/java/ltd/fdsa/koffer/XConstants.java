package ltd.fdsa.koffer;

/**
 * 常量表
 */
public interface XConstants {
    String BOOT_INF_CLASSES = "BOOT-INF/classes/";
    String BOOT_INF_LIB = "BOOT-INF/lib/";

    String META_INF_MANIFEST = "META-INF/MANIFEST.MF";
    String Koffer_SRC_DIR = XConstants.class.getPackage().getName().replace('.', '/') + "/";
    String Koffer_INF_DIR = "Koffer-INF/";
    String Koffer_INF_IDX = "INDEXES.IDX";
    String CRLF = System.getProperty("line.separator");

    String DEFAULT_ALGORITHM = "AES/CBC/PKCS5Padding";
    int DEFAULT_KEYSIZE = 128;
    int DEFAULT_IVSIZE = 128;
}
