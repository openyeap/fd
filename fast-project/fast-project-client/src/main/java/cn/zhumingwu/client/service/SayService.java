package cn.zhumingwu.client.service;


import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
public abstract class SayService implements PersonService {
    List<String> data = new ArrayList<>();

    @Override
    public void say(String message) {
        var list = IntStream.rangeClosed(1, 1000000).mapToObj(m -> m + ":" + message).toArray(String[]::new);
        data.addAll(Lists.asList("", list));
        log.info("I'm {} size:{}", this, data.size());
    }
}