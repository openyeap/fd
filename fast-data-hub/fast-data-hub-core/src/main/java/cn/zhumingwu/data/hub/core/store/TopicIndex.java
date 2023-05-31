package cn.zhumingwu.data.hub.core.store;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import cn.zhumingwu.data.hub.core.util.FileChannelUtil;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.*;

/**
 * The skip map of topic offset and file position
 * <p>
 * offset vint
 * size vint
 * fileId
 * position vint
 *
 * @author zhumingwu
 * @since 2022/1/27 17:41
 */
@Slf4j
public class TopicIndex {
    private static final Cache<String, TopicIndex> FILE_HANDLER = CacheBuilder.newBuilder().maximumSize(1024)
            .expireAfterAccess(Duration.ofMinutes(10))
            .build();
    private static final Map<String, Object> topics = new HashMap<>();
    private final SortedOffset offsets;

    private TopicIndex(SortedOffset offsets) {
        this.offsets = offsets;
    }

    public SortedOffset.OffsetElement getLast() {
        return this.offsets.get(offsets.size()-1);
    }

    public SortedOffset.OffsetElement getFirst() {
        return this.offsets.get(0);
    }

    public SortedOffset.OffsetElement search(long offset) {
        var index = this.offsets.search(offset);
        if (index >= 0) {
            return this.offsets.get(index);
        }
        return null;
    }

    public Set<String> topics() {
        return topics.keySet();
    }

    public static TopicIndex loadTopicIndex(String key) {
        var result = FILE_HANDLER.getIfPresent(key);
        if (result != null) {
            return result;
        }
        var path = Paths.get("./data", "index", key + ".index");
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
                FILE_HANDLER.put(key, new TopicIndex(new SortedOffset()));
            } else {

                var fileChannel = FileChannel.open(path, StandardOpenOption.READ);
                SortedOffset list = new SortedOffset();
                while (true) {
                    // offset and file id
                    var offset = FileChannelUtil.readVLen(fileChannel);
                    if (offset <= 0) {
                        break;
                    }
                    var size = FileChannelUtil.readVLen(fileChannel);
                    if (size <= 0) {
                        break;
                    }
                    var fileId = FileChannelUtil.readVLen(fileChannel);
                    var position = FileChannelUtil.readVLen(fileChannel);
                    var element = new SortedOffset.OffsetElement(offset, (int) size, fileId, position);
                    list.add(element);
                }
                fileChannel.close();
                list.sort();
                FILE_HANDLER.put(key, new TopicIndex(list));
            }
        } catch (IOException e) {
            log.error("error", e);
            return null;
        }
        return FILE_HANDLER.getIfPresent(key);
    }
}
