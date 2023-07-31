package cn.zhumingwu.base.serializer;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import lombok.extern.slf4j.Slf4j;


import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Slf4j
public class HessianSerializer implements Serializer {
    @Override
    public String serialize(Object obj) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Hessian2Output output = new Hessian2Output(outputStream);
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
            Hessian2Input input = new Hessian2Input(new BufferedInputStream(inputStream));
            var result =(T)  input.readObject();
            input.close();
            return result;
        } catch (IOException e) {
            log.error("deserialize failed:", e);
            return null;
        }
    }
}
