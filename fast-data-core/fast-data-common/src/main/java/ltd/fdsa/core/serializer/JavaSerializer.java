package ltd.fdsa.core.serializer;

import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.io.*;
import java.util.Base64;

@Slf4j
public class JavaSerializer implements Serializer {
    @Override
    public String serialize(Object obj) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ObjectOutputStream output = new ObjectOutputStream(outputStream)) {
            output.writeObject(obj);
            output.close();
            var result = Base64.getEncoder().encodeToString(outputStream.toByteArray());
            log.info("serialize:{}", result);
            return result;
        } catch (IOException e) {
            log.error("serialize failed:", e);
            return "";
        }
    }

    @Override
    public <T> T deserialize(String data, Class<T> clazz) {
        log.info("deserialize:{}", data);
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data))) {
            ObjectInputStream input = new ObjectInputStream(new BufferedInputStream(inputStream));
            var result = (T) input.readObject();
            input.close();
            return result;
        } catch (IOException | ClassNotFoundException e) {
            log.error("deserialize failed:", e);
            return null;
        }
    }
}
