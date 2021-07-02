package ltd.fdsa.client.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SayHello extends SayService {
    @Override
    public void say(String message) {
        super.say("Hello, " + message);
    }
}

