package cn.zhumingwu.data.hub.core.serializer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;


/**
 * Custom serializer for arbitrary Java classes.
 * <p>
 * Users should register serializers in the configuration:
 * <pre>
 * Configuration configuration = PalDB.newConfiguration();
 * configuration.registerSerializer(new PointSerializer());
 * </pre>
 *
 * @param <T> class type
 */
public interface Serializer<T> extends Serializable {


    /**
     * Writes the instance <code>input</code> to the data output.
     *
     * @param dataOutput data output
     * @param input      instance
     * @throws IOException if an io error occurs
     */
    public void write(DataOutput dataOutput, T input) throws IOException;

    /**
     * Reads the data input and creates the instance.
     *
     * @param dataInput data input
     * @return new instance of type <code>K</code>.
     * @throws IOException if an io error occurs
     */
    public T read(DataInput dataInput) throws IOException;

    /**
     * Serializes the input object and returns bytes
     * <p>
     * This is used to get binary of the object's representation
     *
     * @author zhumingwu
     * @since 1/2/2022 上午10:47
     */
    public byte[] serialize(T input);

    /**
     * Returns the object of the identify class from the binary data
     *
     * @author zhumingwu
     * @since 1/2/2022 上午10:50
     */
    public T deserialize(byte[] data, Class<T> clazz);

    /**
     * Returns the estimate number of bytes used to hold <code>instance</code> in memory.
     * <p>
     * This information is used by the cache so it can manages its memory usage.
     *
     * @param instance instance to get weight for
     * @return the number of bytes the object uses in memory
     */
    public int getWeight(T instance);
}
