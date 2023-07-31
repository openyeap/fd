package cn.zhumingwu.data.hub.core.util;



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * file tool
 */
public class VIntUtil {
    public static byte[] vintEncode(long i) {
        try (ByteArrayOutputStream s = new ByteArrayOutputStream();) {
            while ((i & ~0x7F) != 0) {
                s.write((int) ((i & 0x7F) | 0x80));
                i >>>= 7;
            }
            s.write((int) i);

            return s.toByteArray();
        } catch (Exception ex) {
            return new byte[0];
        }
    }
}
