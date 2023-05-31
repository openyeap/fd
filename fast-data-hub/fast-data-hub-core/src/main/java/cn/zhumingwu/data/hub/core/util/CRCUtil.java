package cn.zhumingwu.data.hub.core.util;

import lombok.var;

import java.util.zip.CRC32;

public class CRCUtil {

    final CRC32 crc32 = new CRC32();

    private CRCUtil() {
    }

    public static CRCUtil crc32(byte[] content) {
        CRCUtil crcUtil = new CRCUtil();
        if (content.length > 0) {
            crcUtil.update(content);
        }
        return crcUtil;
    }

    public CRCUtil update(byte[] content) {
        if (content.length > 0) {
            crc32.update(content);
        }
        return this;
    }

    public CRCUtil update(int content) {
        crc32.update(content);
        return this;
    }

    public long getValue() {
        return this.crc32.getValue();
    }

    public byte[] getBytes() {
        var value = this.crc32.getValue();
        byte[] src = new byte[4];
        src[3] = (byte) ((value & 0xFF000000) >> 24);
        src[2] = (byte) ((value & 0x00FF0000) >> 16);
        src[1] = (byte) ((value & 0x0000FF00) >> 8);
        src[0] = (byte) ((value & 0x000000FF));
        return src;
    }

    public static long toLong(byte[] src) {
        int value;
        value = (int) ((src[0] & 0xFF)
                | ((src[1] & 0xFF) << 8)
                | ((src[2] & 0xFF) << 16)
                | ((src[3] & 0xFF) << 24));
        return value;

    }

    public boolean check(byte[] original) {
        return Long.compare(crc32.getValue(), toLong(original)) == 0;
    }

    public boolean check(long original) {
        return Long.compare(crc32.getValue(), original) == 0;
    }
}
