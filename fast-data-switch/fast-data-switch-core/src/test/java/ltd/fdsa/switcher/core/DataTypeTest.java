package ltd.fdsa.switcher.core;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.core.util.FileUtils;
import ltd.fdsa.switcher.core.config.YamlConfig;
import ltd.fdsa.switcher.core.model.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import java.util.Random;


/**
 * @ClassName:
 * @description:
 * @since 2020-10-28
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {String.class})
@Slf4j
public class DataTypeTest {
    @Autowired
    private StandardEnvironment env;

    @Test
    public void TestBoolData() {
        byte input = 1;
        BoolData data = new BoolData(input);
        var bytes = data.toBytes();
        var output = data.parse(bytes).getValue();
        Assert.assertTrue("TestBoolData", (boolean) output);

    }

    @Test
    public void TestBoolData2() {
        byte input = 0;
        BoolData data = new BoolData(input);
        var bytes = data.toBytes();
        var output = data.parse(bytes).getValue();
        Assert.assertFalse("TestBoolData", (boolean) output);
    }


    @Test
    public void TestByteData() {
        var input = (byte) (new Random().nextInt() & 0xFF);
        ByteData data = new ByteData(input);
        var bytes = data.toBytes();
        var output = data.parse(bytes).getValue();
        Assert.assertEquals("TestByteData", input, output);
    }

    @Test
    public void TestCharData() {
        var input = (char) (new Random().nextInt() & 0xFFFF);
        CharData data = new CharData(input);
        var bytes = data.toBytes();
        var output = data.parse(bytes).getValue();
        Assert.assertEquals("TestCharData", input, output);
    }

    @Test
    public void TestStringData() {
        var input = "(中方) (new Random().nextInt() & 0xFFFF)";
        StringData data = new StringData(input);
        var bytes = data.toBytes();
        var output = data.parse(bytes).getValue();
        log.info(input);
        log.info(output.toString());
        Assert.assertEquals("TestStringData", input, output);
    }


    @Test
    public void TestDoubleData() {
        var input = new Random().nextDouble();
        DoubleData data = new DoubleData(input);
        var bytes = data.toBytes();
        var output = data.parse(bytes).getValue();
        Assert.assertEquals("TestIntData", input, output);
    }

    @Test
    public void TestFloatData() {
        var input = new Random().nextFloat();
        FloatData data = new FloatData(input);
        var bytes = data.toBytes();
        var output = data.parse(bytes).getValue();
        Assert.assertEquals("TestFloatData", input, output);
    }

    @Test
    public void TestLongData() {
        var input = new Random().nextLong();
        LongData data = new LongData(input);
        var bytes = data.toBytes();
        var output = data.parse(bytes).getValue();
        Assert.assertEquals("TestLongData", input, output);
    }

    @Test
    public void TestDateData() {
        var input = new Date();
        DateData data = new DateData(input.getTime());
        var bytes = data.toBytes();
        var output = data.parse(bytes).getValue();
        Assert.assertEquals("TestDateData", input, output);
    }
}
