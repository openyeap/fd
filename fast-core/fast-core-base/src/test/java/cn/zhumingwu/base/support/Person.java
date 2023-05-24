package cn.zhumingwu.base.support;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.base.util.NamingUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service("person")
public class Person {
    public void say(String message) {
        NamingUtils.formatLog(log,"Default say: {}", message);
    }
}
