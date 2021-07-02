package ltd.fdsa.job.admin.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static ltd.fdsa.job.admin.util.JacksonUtil.writeValueAsString;

@Slf4j
public class JacksonUtilTest {

    @Test
    public void shouldWriteValueAsString() {
        // given
        Map<String, String> map = new HashMap<>();
        map.put("aaa", "111");
        map.put("bbb", "222");

        // when
        String json = writeValueAsString(map);
    }

    @Test
    public void shouldReadValueAsObject() {
        // given
        String jsonString = "{\"aaa\":\"111\",\"bbb\":\"222\"}";

        // when
        Map result = JacksonUtil.readValue(jsonString, Map.class);

    }
    private static volatile Map<Integer, List<Integer>> ringData = new ConcurrentHashMap<>();

    @Test
    public void aaaa() {
        // second data
        List<Integer> ringItemData = new ArrayList<>();
        int nowSecond =
                Calendar.getInstance().get(Calendar.SECOND); // 避免处理耗时太长，跨过刻度，向前校验一个刻度；
        for (int i = 0; i < 2; i++) {
            List<Integer> tmpData = ringData.remove((nowSecond + 60 - i) % 60);
            if (tmpData != null) {
                ringItemData.addAll(tmpData);
            }
        }



    }
}
