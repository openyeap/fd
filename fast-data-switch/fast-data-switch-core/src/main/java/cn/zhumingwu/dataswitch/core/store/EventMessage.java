package cn.zhumingwu.dataswitch.core.store;

import com.google.iot.cbor.CborMap;
import com.google.iot.cbor.CborTextString;
import com.lmax.disruptor.AggregateEventHandler;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import lombok.var;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * represents the message data
 * <p>
 *
 * @author zhumingwu
 * @since 2022/1/27 17:41
 */
public class EventMessage {
    private final String topic;
    private final long timestamp;
    private final CborMap header;
    private final byte[] payload;

    public EventMessage(String topic, byte[] payload, long timestamp, Map<String, String> header) {
        this.topic = topic;
        this.timestamp = timestamp;
        this.header = CborMap.create();
        for (var entry : header.entrySet()) {
            this.header.put(entry.getKey(), CborTextString.create(entry.getValue()));
        }
        this.payload = payload;
    }

    public EventMessage(String topic, byte[] payload) {
        this.topic = topic;
        this.timestamp = System.currentTimeMillis();
        this.header = CborMap.create();
        this.payload = payload;
    }


    public long getTimestamp() {
        return this.timestamp;
    }

    public String getTopic() {
        return this.topic;
    }

    public CborMap getHeader() {
        return this.header;
    }

    public byte[] getPayload() {
        return this.payload;
    }
}