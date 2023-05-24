package cn.zhumingwu.base.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Slf4j
public class KryoSerializer implements Serializer {

    @Override
    public String serialize(Object obj) {
        Kryo kryo = kryoLocal.get();
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Output output = new Output(outputStream);//<1>
            kryo.writeObject(output, obj);//<2>
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
        Kryo kryo = kryoLocal.get();
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data))) {
            Input input = new Input(inputStream);// <1>
            var result = kryo.readObject(input, clazz);//<2>
            input.close();
            return result;
        } catch (IOException e) {
            log.error("deserialize failed:", e);
            return null;
        }
    }

    private static final ThreadLocal<Kryo> kryoLocal = new ThreadLocal<Kryo>() {//<3>
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            kryo.setReferences(true);//默认值为true,强调作用
            kryo.setRegistrationRequired(false);//默认值为false,强调作用
            return kryo;
        }
    };

}
