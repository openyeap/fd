package ltd.fdsa.influxdb.model;

public interface Region {
    String toRegion();

    default String getType() {
        return this.getClass().getSimpleName();
    }

    default boolean getStrict() {
        return true;
    }
}
