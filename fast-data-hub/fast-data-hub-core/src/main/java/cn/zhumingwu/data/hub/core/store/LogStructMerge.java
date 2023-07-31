package cn.zhumingwu.data.hub.core.store;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import lombok.extern.slf4j.Slf4j;

import cn.zhumingwu.data.hub.core.util.CRCUtil;
import cn.zhumingwu.data.hub.core.util.FileChannelUtil;
import cn.zhumingwu.data.hub.core.util.VIntUtil;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;


/**
 * LSM, the log-structured merge-tree is a data structure with performance characteristics that make it attractive for providing indexed access to file with high insert volume
 * LSM trees maintain data in two or more separate structures, each of which is optimized for its respective underlying storage medium;
 *
 * <p>data is synchronized between the two structures efficiently, in batches.
 *
 * @author zhumingwu
 * @since 1.0.0
 */
@Slf4j
public class LogStructMerge {

    static Cache<String, LogStructMerge> FILE_HANDLER = CacheBuilder.newBuilder().maximumSize(1024)
            .expireAfterAccess(Duration.ofMinutes(10)).removalListener(new RemovalListener<String, LogStructMerge>() {
                @Override
                public void onRemoval(RemovalNotification<String, LogStructMerge> removalNotification) {
                    var lsm = removalNotification.getValue();
                    if (lsm != null) {
                        lsm.close();
                    }
                }
            })
            .build();

    private final FileChannel lsmFile;
    private final String topic;
    private final byte version;
    private final byte status;
    private final long offset;
    private final long start;
    private int size;
    private int end;

    LogStructMerge(FileChannel lsmChannel, String topic) throws IOException {
        this.topic = topic;
        this.lsmFile = lsmChannel;
        byte[] header = FileChannelUtil.read(this.lsmFile, 2);
        this.version = header[0];
        this.status = header[1];
        this.offset = FileChannelUtil.readLong(this.lsmFile);
        this.start = FileChannelUtil.readLong(this.lsmFile);
        FileChannelUtil.newPosition(this.lsmFile, -8);
        this.size = FileChannelUtil.readInt(this.lsmFile);
        this.end = FileChannelUtil.readInt(this.lsmFile);
    }

    public static LogStructMerge getInstance(String topic, long fileId) {
        var key = topic + "_" + fileId;
        var result = FILE_HANDLER.getIfPresent(key);
        if (result != null) {
            return result;
        }
        var path = Paths.get("./data", "lsm", topic, fileId + ".lsm");
        if (!Files.exists(path)) {
            return null;
        }
        try {
            var fileChannel = FileChannel.open(path, StandardOpenOption.READ);
            result = new LogStructMerge(fileChannel, topic);
            FILE_HANDLER.put(key, result);
        } catch (IOException e) {
            log.error("error", e);
            return null;
        }

        return result;
    }

    public EventMessage read(long position, Long offsetExcept) {
        try {
            FileChannelUtil.newPosition(this.lsmFile, position);
            var status = FileChannelUtil.read(this.lsmFile, 1)[0];
            var size = FileChannelUtil.readVLen(this.lsmFile);
            if (status == -1) {
                return read(this.lsmFile.position() + size, offsetExcept);
            }
            var offset = FileChannelUtil.readVLen(this.lsmFile) + this.offset;
            if (offset <= offsetExcept) {
                return read(this.lsmFile.position() + size, offsetExcept);
            }
            var timestamp = FileChannelUtil.readVLen(this.lsmFile) + this.start;
            var schema = (int) FileChannelUtil.readVLen(this.lsmFile);
            var payload = FileChannelUtil.readVarByte(this.lsmFile);
            var crc = FileChannelUtil.readInt(this.lsmFile);

            if (CRCUtil.crc32().update(offset).update( timestamp).update(payload).check(crc)) {
                return new EventMessage(this.topic, payload, schema, timestamp);
            }
        } catch (IOException e) {
            log.error("error", e);
        }
        return null;
    }

    public boolean write(EventMessage message, long offset) {
        var offsetDelta = VIntUtil.vintEncode(offset - this.offset);
        var timestampDelta = VIntUtil.vintEncode(message.getTimestamp() - this.start);
        var schema = VIntUtil.vintEncode(message.getSchema());
        var payload = message.getPayload();
        var payloadLength = VIntUtil.vintEncode(payload.length);

        var crc = CRCUtil.crc32().update(offset).update(message.getTimestamp()).update(payload).getBytes();

        try {
            FileChannelUtil.newPosition(this.lsmFile, -8);
            //status
            FileChannelUtil.writeByte(this.lsmFile, new byte[]{1});
            //size
            var contentLength = VIntUtil.vintEncode(offsetDelta.length + timestampDelta.length + schema.length + payloadLength.length + payload.length + 4);
            FileChannelUtil.writeByte(this.lsmFile, contentLength);
            //offset
            FileChannelUtil.writeByte(this.lsmFile, offsetDelta);
            //timestamp
            FileChannelUtil.writeByte(this.lsmFile, timestampDelta);
            //schema
            FileChannelUtil.writeByte(this.lsmFile, schema);
            //payload
            FileChannelUtil.writeByte(this.lsmFile, payloadLength);
            FileChannelUtil.writeByte(this.lsmFile, payload);
            //crc
            FileChannelUtil.writeByte(this.lsmFile, crc);
            this.size++;
            this.end = (int) (message.getTimestamp() - this.start);
            FileChannelUtil.writeInt(this.lsmFile, this.size);
            FileChannelUtil.writeInt(this.lsmFile, this.end);
            return true;
        } catch (IOException e) {
            log.error("error", e);
            return false;
        }
    }

    public void close() {
        try {
            this.lsmFile.close();
        } catch (IOException e) {
            log.error("error", e);
        }
    }
}
