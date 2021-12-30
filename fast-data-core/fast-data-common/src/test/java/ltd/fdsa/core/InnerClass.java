package ltd.fdsa.core;

import lombok.Data;
import ltd.fdsa.core.support.MyClass;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.time.Duration;

@MyClass(value = "test")
@Data
@ConfigurationProperties(prefix = "inner")
public class InnerClass implements Serializable {
    private String age;
    private String name;
    private Duration delay;
}
