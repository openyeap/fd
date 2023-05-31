package cn.zhumingwu.data.hub.core.store;

import lombok.var;

import java.util.HashMap;
import java.util.Map;

/**
 * Memory Table to cache data
 * <p>fast to get recent data from memory.
 *
 * @author zhumingwu
 * @since 2022/1/27 17:37
 */
public class EventMemoryTable {
    // memory buffer cache
    private final Map<String, EventRingBuffer> ringBufferMap;
    private final WriteAppendLog wal;

    public EventMemoryTable() {
        // init ring buffer
        this.ringBufferMap = new HashMap<>();
        this.wal = WriteAppendLog.loadWriteAppendLog();
    }

    public long put(EventMessage message) {
        var offset = -1L;
        var key = message.getTopic();
        var buffer = this.ringBufferMap.get(key);
        if (buffer == null) {
            var topicIndex = TopicIndex.loadTopicIndex(key);
            if (topicIndex != null) {
                var offsetElement = topicIndex.getLast();
                if (offsetElement != null) {
                    offset = offsetElement.offset + offsetElement.size + 1;
                }
            }
            this.ringBufferMap.put(key, new EventRingBuffer(offset, 10));
        }
        buffer = this.ringBufferMap.get(key);
        offset = buffer.push(message);
        if (offset < 0) {
            return offset;
        }
        message.setOffset(offset);
        this.wal.append(message);
        return offset;
    }

    public EventMessage get(String key, long offset) {
        if (!this.ringBufferMap.containsKey(key)) {
            return getFromFile(key, offset);
        }
        // 最近的数据
        var buffer = this.ringBufferMap.get(key);
        var result = buffer.pull(offset);
        if (result == null) {
            // 读取文件
            return getFromFile(key, offset);
        }
        return result;
    }

    private EventMessage getFromFile(String key, long offset) {
        // 读取topic文件
        var topicIndex = TopicIndex.loadTopicIndex(key);
        if (topicIndex == null) {
            return null;
        }
        var offsetElement = topicIndex.search(offset);
        if (offsetElement == null) {
            return null;
        }
        // 历史数据中取
        var file = LogStructMerge.getInstance(key, offsetElement.fileId);
        if (file == null) {
            return null;
        }
        return file.read(offsetElement.position, offset);
    }
}
