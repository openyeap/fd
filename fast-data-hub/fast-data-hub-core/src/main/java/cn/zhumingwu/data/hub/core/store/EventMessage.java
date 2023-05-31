package cn.zhumingwu.data.hub.core.store;


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
    private final int schema;
    private long offset;
    private final byte[] payload;

    public EventMessage(String topic, byte[] payload, int schema, long timestamp) {
        this.topic = topic;
        this.schema = schema;
        this.timestamp = timestamp;
        this.payload = payload;
    }

    public EventMessage(String topic, byte[] payload, int schema) {
        this.topic = topic;
        this.schema = schema;
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

    public int getSchema() {
        return this.schema;
    }
}