package ltd.fdsa.core.event;

import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.util.Map;


@ToString
public class RefreshedEvent extends ApplicationEvent {
    private final Map<String, String> data;

    public RefreshedEvent(Object source, Map<String, String> data) {
        super(source);
        this.data = data;
    }

    public Map<String, String> getData() {
        return data;
    }

    public RefreshedEvent() {
        super("");
        this.data = null;
    }
}
