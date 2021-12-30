package ltd.fdsa.core.event;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.core.serializer.JsonSerializer;
import ltd.fdsa.core.serializer.Serializer;
import org.springframework.context.ApplicationEvent;

@Slf4j
public class RemotingEvent extends ApplicationEvent {
    public static Serializer SERIALIZER = new JsonSerializer();
    /**
     * 目标事件
     */
    private final ApplicationEvent target;

    public ApplicationEvent getTarget() {
        return target;
    }

    private final String name;

    public RemotingEvent(ApplicationEvent target) {
        super(target.getSource());
        this.target = target;
        this.name = this.target.getClass().getCanonicalName();
    }


    public static <T extends ApplicationEvent> T getApplicationEvent(String payload, Class<T> tClass) {
        try {
            return SERIALIZER.deserialize(payload, tClass);
        } catch (Exception ex) {
            log.error("deserialize failed:", ex);
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public String getPayload() {

        try {
            var result = SERIALIZER.serialize(this.target);
            return result;
        } catch (Exception ex) {
            log.error("serialize failed:", ex);
            return "";
        }
    }
}
