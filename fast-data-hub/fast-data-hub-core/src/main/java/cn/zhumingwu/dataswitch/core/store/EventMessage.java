package cn.zhumingwu.dataswitch.core.store;


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
    private long offset;
    private final byte[] payload;

    public EventMessage(String topic, byte[] payload, long timestamp) {
        this.topic = topic;
        this.timestamp = timestamp;
        this.payload = payload;
    }

    public EventMessage(String topic, byte[] payload) {
        this.topic = topic;
        this.timestamp = System.currentTimeMillis();
        this.payload = payload;
    }


    public long getTimestamp() {
        return this.timestamp;
    }

    public String getTopic() {
        return this.topic;
    }


    public byte[] getPayload() {
        return this.payload;
    }

    public long getOffset() {
        return this.offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }
}