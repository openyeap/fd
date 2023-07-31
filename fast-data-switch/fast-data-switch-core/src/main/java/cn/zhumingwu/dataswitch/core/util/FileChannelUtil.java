package cn.zhumingwu.dataswitch.core.util;



import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * file tool
 */
public class FileChannelUtil {
    public static void newPosition(FileChannel fileChannel, long position) throws IOException {
        if (position >= 0) {
            fileChannel.position(position);
        } else {
            position += fileChannel.size();
            fileChannel.position(position);
        }
    }

    public static boolean writeVLen(FileChannel fileChannel, long length) throws IOException {
        var data = VIntUtil.vintEncode(length);
        return writeByte(fileChannel, data);
    }

    public static boolean writeVarByte(FileChannel fileChannel, byte[] data) throws IOException {
        var length = VIntUtil.vintEncode(data.length);
        writeByte(fileChannel, length);
        writeByte(fileChannel, data);
        return true;
    }


    public static boolean writeByte(FileChannel fileChannel, byte[] data) throws IOException {
        if (data.length == 0) {
            return false;
        }
        var byteBuffer = ByteBuffer.allocate(data.length);
        byteBuffer.put(data);
        fileChannel.write(byteBuffer);
        return true;
    }

    public static boolean writeLong(FileChannel fileChannel, long data) throws IOException {
        var byteBuffer = ByteBuffer.allocate(Long.BYTES);
        byteBuffer.putLong(data);
        fileChannel.write(byteBuffer);
        return true;
    }

    public static boolean writeInt(FileChannel fileChannel, int data) throws IOException {
        var byteBuffer = ByteBuffer.allocate(Integer.BYTES);
        byteBuffer.putInt(data);
        fileChannel.write(byteBuffer);
        return true;
    }


    public static byte[] readVarByte(FileChannel fileChannel) throws IOException {
        var length = readVLen(fileChannel);
        if (length <= 0) {
            return new byte[0];
        }
        return read(fileChannel, (int) length);
    }


    public static long readVLen(FileChannel fileChannel) throws IOException {

        byte b = read(fileChannel, 1)[0];
        if (b <= 0) {
            return 0;
        }
        long i = b & 0x7F;
        var c = 1;
        while (c < 10) {
            b = read(fileChannel, 1)[0];
            i |= (long) (b & 0x7F) << (7 * c);
            if (b >= 0) {
                return i;
            }
            c++;
        }
        return 0;
    }

    public static int readInt(FileChannel fileChannel) throws IOException {
        var byteBuffer = ByteBuffer.allocate(4);
        var read = fileChannel.read(byteBuffer);
        if (read != 4) {
            throw new IOException();
        }
        return byteBuffer.getInt();
    }

    public static long readLong(FileChannel fileChannel) throws IOException {
        var byteBuffer = ByteBuffer.allocate(8);
        var read = fileChannel.read(byteBuffer);
        if (read != 8) {
            throw new IOException();
        }
        return byteBuffer.getLong();

    }

    public static byte[] read(FileChannel fileChannel, int size) throws IOException {

        var byteBuffer = ByteBuffer.allocate(size);
        var read = fileChannel.read(byteBuffer);
        if (read != size) {
            throw new IOException();
        }
        return byteBuffer.array();
    }
}
