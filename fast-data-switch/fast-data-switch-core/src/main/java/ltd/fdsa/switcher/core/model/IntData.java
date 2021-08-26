package ltd.fdsa.switcher.core.model;

public class IntData implements Data {
    private int data;

    @Override
    public Object getValue() {
        return this.data;
    }

    @Override
    public Type getType() {
        return Type.INT;
    }
}
