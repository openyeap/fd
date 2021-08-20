package ltd.fdsa.kafka.connect.transform;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.connect.connector.ConnectRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public interface CustomTransform {

    Map<String, Object> apply(Map<String, Object> record);
}
