package ltd.fdsa.switcher.core.model;

public interface Data {

    Object getValue();

    Type getType();
    enum Type {
        Byte, // one byte
        Char, // two bytes
        INT,  // four bytes
        FLOAT, // four bytes
        LONG, // eight bytes
        DOUBLE,// eight bytes
        STRING, // n bytes
        BOOL,   // bool
        DATE,   // eight bytes timestamp
        BYTES   // n bytes
    }
}
