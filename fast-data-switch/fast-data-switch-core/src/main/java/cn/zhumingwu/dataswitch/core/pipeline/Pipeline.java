package cn.zhumingwu.dataswitch.core.pipeline;


import cn.zhumingwu.base.config.Configuration;
import cn.zhumingwu.dataswitch.core.model.Record;

import java.util.Collections;
import java.util.List;

public interface Pipeline {

    default Configuration context() {
        return null;
    }

    // init
    default void init() {
    }

    // start
    default void start() {
    }

    // execute
    void execute(Record... records);

    default List<Pipeline> nextSteps() {
        return Collections.emptyList();
    }

    default void stop() {
    }

    default void stop(Runnable callback) {
        stop();
        callback.run();
    }

    default boolean isRunning() {
        return false;
    }
}
