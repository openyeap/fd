package cn.zhumingwu.dataswitch.core.store;

import cn.zhumingwu.base.config.Configuration;
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
public class EventTable {
    // memory buffer cache
    private final Map<String, EventRingBuffer> ringBufferMap;
    // configuration
    private final Configuration config;
    private final WriteAppendLog wal;

    public EventTable(Configuration config) {
        this.config = config;
        // init ring buffer
        this.ringBufferMap = new HashMap<>();
        this.wal = WriteAppendLog.loadWriteAppendLog();
    }

    public boolean put(String key, EventMessage message) {
        var result = this.wal.append(message);
        if (result) {
            if (!this.ringBufferMap.containsKey(key)) {
                this.ringBufferMap.put(key, new EventRingBuffer(-1, 10));
            }
            var offset = this.ringBufferMap.get(key).push(message);
            var element = TopicIndex.loadTopic(key).getFirst();
            LogStructMerge.getInstance(key, element.fileId).write(message, offset);
            return true;
        }
        return false;
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
        var topicIndex = TopicIndex.loadTopic(key);
        var offsetElement = topicIndex.search(offset);
        if (offsetElement == null) {
            return null;
        }
        var fileId = offsetElement.position;
        var position = offsetElement.position;
        // 历史数据中取
        return LogStructMerge.getInstance(key, fileId).read(position, offset);
    }
}
