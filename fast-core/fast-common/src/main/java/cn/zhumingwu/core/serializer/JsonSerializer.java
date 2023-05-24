package cn.zhumingwu.core.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class JsonSerializer implements Serializer {
    final static ObjectMapper mapper = new ObjectMapper();

    @Override
    public String serialize(Object obj) {
        try {
            var result = mapper.writeValueAsString(obj);
            log.info("serialize:{}", result);
            return result;
        } catch (IOException e) {
            log.error("serialize failed:", e);
            return "";
        }
    }

    @Override
    public <T> T deserialize(String data, Class<T> clazz) {
        log.info("deserialize:{}", data);
        try {
            return mapper.readValue(data.getBytes(StandardCharsets.UTF_8), clazz);
        } catch (IOException e) {
            log.error("deserialize failed:", e);
            return null;
        }
    }
}
