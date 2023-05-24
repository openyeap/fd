package cn.zhumingwu.base;

import cn.zhumingwu.base.support.MyClass;
import lombok.Data;
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
