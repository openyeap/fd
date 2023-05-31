package cn.zhumingwu.dataswitch.core.store;

import lombok.var;
import cn.zhumingwu.dataswitch.core.util.CRCUtil;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Fast to write message data into wal file
 * <p>
 *     使用内存映射文件快速将消息写到日志文件中
 * <p>
 * offset - 8 byte
 * timestamp - 8 byte
 * topic - var byte length + 4
 * payload - var byte length + 4
 * crc - 4 byte
 *
 * @author zhumingwu
 * @since 2022/1/27 17:40
 */
public class WriteAppendLog {
    private static WriteAppendLog writeAppendLog;
    private final static int WAL_SIZE = 256 * 1024 * 1024;
    private final static short WRITE_APPEND_LOG_SIZE = 4;
    private final static MappedByteBuffer[] WRITE_APPEND_LOGS = new MappedByteBuffer[WRITE_APPEND_LOG_SIZE];
    private int counter = 0;
    private final AtomicInteger size = new AtomicInteger(0);
    private final Lock lock = new ReentrantLock();

    public static WriteAppendLog loadWriteAppendLog() {
        if (writeAppendLog == null) {
            for (int i = 0; i < WRITE_APPEND_LOG_SIZE; i++) {
                var path = Paths.get("./", "data", "wal", i + ".wal");
                try {
                    var channel = FileChannel.open(path, StandardOpenOption.CREATE_NEW);
                    WRITE_APPEND_LOGS[i] = channel.map(FileChannel.MapMode.READ_WRITE, 0, WAL_SIZE);
                } catch (IOException ignored) {
                }
            }
            writeAppendLog = new WriteAppendLog();
        }
        return writeAppendLog;
    }

    private WriteAppendLog() {
    }
    private MappedByteBuffer ensureCapacityInternal(int length) {

        if (this.size.get() + length >= WAL_SIZE) {
            lock.lock();
            //如果当前文件空间不足
            try {
                if (this.size.get() + length >= WAL_SIZE) {
                    //重置当前文件路径
                    WRITE_APPEND_LOGS[counter & WRITE_APPEND_LOG_SIZE].position(0);
                    this.counter++;
                    this.size.set(0);
                }
            } finally {
                lock.unlock();
            }
        }
        this.size.addAndGet(length);
        return WRITE_APPEND_LOGS[counter & WRITE_APPEND_LOG_SIZE];
    }

    public boolean append(EventMessage message) {
        var offset = message.getOffset();
        var timestamp = message.getTimestamp();
        var topic = message.getTopic().getBytes(StandardCharsets.UTF_8);
        var payload = message.getPayload();
        var length = 34 + topic.length + payload.length;
        var data = ByteBuffer.allocate(length);
        data.putLong(offset);       //8
        data.putLong(timestamp);    //8
        data.putInt(topic.length);  //4
        data.put(topic);            //topic.length
        data.putInt(payload.length);//4
        data.put(payload);          //payload.length;
        var buffer = this.ensureCapacityInternal(length + 4);
        buffer.put(data.array());
        buffer.put(CRCUtil.crc32(data.array()).getBytes());
        buffer.force();
        return true;
    }
}