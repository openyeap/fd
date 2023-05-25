package cn.zhumingwu.dataswitch.core.store;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.var;
import cn.zhumingwu.dataswitch.core.util.FileChannelUtil;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.*;

/**
 * The map of topic and file id
 * <p>fast to get file id of one topic
 *
 * @author zhumingwu
 * @since 2022/1/27 17:41
 */
public class TopicIndex {
    private static Cache<String, TopicIndex> FILE_HANDLER = CacheBuilder.newBuilder().maximumSize(1024)
            .expireAfterAccess(Duration.ofMinutes(10))
            .build();
    private static Map<String, Object> topics = new HashMap<>();
    private final SortedOffset offsets;

    private TopicIndex(SortedOffset offsets) {
        this.offsets = offsets;
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

    public static TopicIndex loadTopic(String key) {
        if (FILE_HANDLER.getIfPresent(key) == null) {
            var path = Paths.get("./", "data", "index", key + ".index");
            try {
                if (!Files.exists(path)) {
                    Files.createFile(path);
                }
                var fileChannel = FileChannel.open(path, StandardOpenOption.READ);

                SortedOffset map = new SortedOffset();
                while (true) {
                    // offset and file id
                    var offset = FileChannelUtil.readVLen(fileChannel);
                    if (offset <= 0) {
                        break;
                    }
                    var size = FileChannelUtil.readVLen(fileChannel);
                    var fileId = FileChannelUtil.readVLen(fileChannel);
                    var position = FileChannelUtil.readVLen(fileChannel);
                    var element = new SortedOffset.OffsetElement(offset, (int) size, fileId, position);
                    map.add(element);
                }
                map.sort();
                fileChannel.close();
                FILE_HANDLER.put(key, new TopicIndex(map));
            } catch (IOException e) {
                return null;
            }
        }
        return FILE_HANDLER.getIfPresent(key);

    }
}
