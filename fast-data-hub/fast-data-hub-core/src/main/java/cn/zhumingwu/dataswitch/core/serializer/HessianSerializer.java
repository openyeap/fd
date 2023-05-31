package cn.zhumingwu.dataswitch.core.serializer;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.io.*;

@Slf4j
public class HessianSerializer implements Serializer<Object> {

    @Override
    public void write(DataOutput dataOutput, Object input) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Hessian2Output output = new Hessian2Output(outputStream);
            output.writeObject(input);
            dataOutput.write(outputStream.toByteArray());
            output.close();
        } catch (IOException e) {
            log.error("serialize failed:", e);
        }
    }

    @Override
    public Object read(DataInput dataInput) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            var b = dataInput.readByte();
            outputStream.write(b);
        } catch (IOException e) {
            log.error("error", e);
        }

        try {

            Hessian2Input input = new Hessian2Input(new ByteArrayInputStream(outputStream.toByteArray()));
            var result = input.readObject();
            input.close();
            return result;
        } catch (IOException e) {
            log.error("deserialize failed:", e);
            return null;
        }
    }

    @Override
    public byte[] serialize(Object input) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Hessian2Output output = new Hessian2Output(outputStream);
            output.writeObject(input);
            output.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            log.error("serialize failed:", e);
            return new byte[0];
        }
    }

    @Override
    public Object deserialize(byte[] data, Class<Object> clazz) {
        try {
            Hessian2Input input = new Hessian2Input(new ByteArrayInputStream(data));
            var result = input.readObject();
            input.close();
            return result;
        } catch (IOException e) {
            log.error("deserialize failed:", e);
            return null;
        }
    }

    @Override
    public int getWeight(Object instance) {
        return 0;
    }
}