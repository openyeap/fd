package ltd.fdsa.kafka.connect.transform;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class DebugTransform implements CustomTransform {
    @Override
    public Map<String, Object> apply(Map<String, Object> record) {
        log.debug(record.toString());
        return record;
    }
}
