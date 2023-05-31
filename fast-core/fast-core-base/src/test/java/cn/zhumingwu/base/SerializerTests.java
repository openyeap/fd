package cn.zhumingwu.base;

import com.caucho.hessian.io.Hessian2Output;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import cn.zhumingwu.base.config.ProjectAutoConfiguration;
import cn.zhumingwu.base.event.RefreshedEvent;
import cn.zhumingwu.base.event.RemotingEvent;
import cn.zhumingwu.base.serializer.HessianSerializer;
import cn.zhumingwu.base.serializer.JavaSerializer;
import cn.zhumingwu.base.serializer.JsonSerializer;
import cn.zhumingwu.base.serializer.KryoSerializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ProjectAutoConfiguration.class)
public class SerializerTests {
    long startTime;

    @Test
    public void Test_Kryo() {
        startTime = System.nanoTime();
        RemotingEvent.SERIALIZER = new KryoSerializer();
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 2; i++) {
            map.put("key" + i, "value" + i);
        }
        var target = new RefreshedEvent(this, map);
        RemotingEvent event = new RemotingEvent(target);
        log.info("length:{},data:{}", event.getPayload().length(), event.getPayload());
        log.info("{} ns", (System.nanoTime() - startTime) / 1000);

    }

    @Test
    public void Test_Hession() {
        startTime = System.nanoTime();
        RemotingEvent.SERIALIZER = new HessianSerializer();
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 2; i++) {
            map.put("key" + i, "value" + i);
        }
        var target = new RefreshedEvent(this, map);
        RemotingEvent event = new RemotingEvent(target);
        log.info("length:{},data:{}", event.getPayload().length(), event.getPayload());
        log.info("{} ns", (System.nanoTime() - startTime) / 1000);

    }

    @Test
    public void Test_Java() {
        startTime = System.nanoTime();
        RemotingEvent.SERIALIZER = new JavaSerializer();
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 2; i++) {
            map.put("key" + i, "value" + i);
        }
        var target = new RefreshedEvent(this, map);
        RemotingEvent event = new RemotingEvent(target);
        log.info("length:{},data:{}", event.getPayload().length(), event.getPayload());
        log.info("{} ns", (System.nanoTime() - startTime) / 1000);

    }

    @Test
    public void Test_Json() {
        startTime = System.nanoTime();
        RemotingEvent.SERIALIZER = new JsonSerializer();
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 2; i++) {
            map.put("key" + i, "value" + i);
        }
        var target = new RefreshedEvent(this, map);
        RemotingEvent event = new RemotingEvent(target);
        log.info("length:{},data:{}", event.getPayload().length(), event.getPayload());
        log.info("{} ns", (System.nanoTime() - startTime) / 1000);

    }

    // @Test
    public void Test_Serializable() throws IOException {

        var targetEvent = new InnerClass();
        targetEvent.setName("test");
        targetEvent.setAge(18 + "y");
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
                objectOutputStream.writeObject(targetEvent);
                var data = outputStream.toByteArray();
                log.info("length:{},data:{}", data.length, new String(data));

                var sss = Base64.getEncoder().encodeToString(data);
                data = sss.getBytes(StandardCharsets.UTF_8);

                log.info("length:{},data:{}", data.length, new String(data));

            } catch (IOException e) {
                log.error("error", e);
            }
        } catch (IOException e) {
            log.error("error", e);
        }
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            Hessian2Output output = new Hessian2Output(os);
            output.writeObject(targetEvent);
            output.close();
            var data = os.toByteArray();
            log.info("length:{},data:{}", data.length, new String(data));

        } catch (IOException e) {
            log.error("error", e);
        }

        Kryo kryo = new Kryo();
        kryo.register(InnerClass.class);
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            Output output = new Output(os);
            kryo.writeObject(output, targetEvent);
            output.close();
            var data = os.toByteArray();
            log.info("length:{},data:{}", data.length, new String(data));
        } catch (IOException e) {
            log.error("error", e);
        }
    }

}
