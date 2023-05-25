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

/**
 * Fast to write message data into wal file
 * <p>
 * crc - 4 byte
 * timestamp - 8 byte
 * payload - var byte
 *
 * @author zhumingwu
 * @since 2022/1/27 17:40
 */
public class WriteAppendLog {
    private static WriteAppendLog writeAppendLog;
    private final static int WAL_SIZE = 256 * 1024 * 1024;
    private final static short WRITE_APPEND_LOG_SIZE = 4;
    private final static MappedByteBuffer[] WRITE_APPEND_LOGS = new MappedByteBuffer[WRITE_APPEND_LOG_SIZE];

    private final AtomicLong counter;
    private final AtomicInteger size;

    public static WriteAppendLog loadWriteAppendLog() {
        if (writeAppendLog == null) {
            writeAppendLog = new WriteAppendLog();
        }
        return writeAppendLog;
    }

    private WriteAppendLog() {
        this.counter = new AtomicLong(0);
        this.size = new AtomicInteger(0);
        for (int i = 0; i < WRITE_APPEND_LOG_SIZE; i++) {
            var path = Paths.get("./", "data", "wal", i + ".wal");
            try {
                var channel = FileChannel.open(path, StandardOpenOption.CREATE_NEW);

                WRITE_APPEND_LOGS[i] = channel.map(FileChannel.MapMode.READ_WRITE, 0, WAL_SIZE);

            } catch (IOException e) {
            }
        }
    }

    private void ensureCapacityInternal(int length) {
        if (this.size.get() + length >= WAL_SIZE) {
            this.counter.incrementAndGet();
            this.size.set(0);
            var file = getCurrentFile();
            file.position(0);
        } else {
            this.size.addAndGet(length);
        }
    }

    MappedByteBuffer getCurrentFile() {
        var index = (int) this.counter.get() & WRITE_APPEND_LOG_SIZE;
        return WRITE_APPEND_LOGS[index];
    }

    public boolean append(EventMessage message) {
        var timestamp = message.getTimestamp();
        var header = message.getHeader().toCborByteArray();
        var topic = message.getTopic().getBytes(StandardCharsets.UTF_8);
        var payload = message.getPayload();
        var length = header.length + topic.length + payload.length + 20;
        ensureCapacityInternal(length);
        var data = ByteBuffer.allocate(length);
        data.putLong(timestamp);
        data.putInt(topic.length);
        data.put(topic);
        data.putInt(header.length);
        data.put(header);
        data.putInt(payload.length);
        data.put(payload);
        var buffer = this.getCurrentFile();
        buffer.put(data.array());
        buffer.put(CRCUtil.crc32(data.array()).getBytes());
        return true;
    }
}



