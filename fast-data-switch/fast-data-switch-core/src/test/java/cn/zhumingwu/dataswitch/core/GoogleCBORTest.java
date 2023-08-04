package cn.zhumingwu.dataswitch.core;

import com.google.iot.cbor.*;
import lombok.extern.slf4j.Slf4j;



import java.nio.charset.StandardCharsets;

@Slf4j
public class GoogleCBORTest {


    @Test
    public void testGoogleCBOR() throws CborParseException {
        byte[] cborBytes = new byte[]{(byte) 0xd9, (byte) 0xd9, (byte) 0xf7,
                (byte) 0xa2, 0x61, 0x61, 0x01, 0x61,
                0x62, (byte) 0x82, 0x02, 0x03};
        CborMap map = CborMap.createFromCborByteArray(cborBytes);
        // Prints out the line `toString: 55799({"a":1,"b":[2,3]})`
        System.out.println("toString: " + map.toString());

        // Prints out the line `toJsonString: {"a":1,"b":[2,3]}`
        System.out.println("toJsonString: " + map.toJsonString());

        CborArray cborArray = (CborArray) map.get("b");
        System.out.println(map.get("a"));
        float sum = 0;

        // Prints out `b: 2` and `b: 3`
        for (CborObject obj : cborArray) {
            System.out.println("b: " + obj);

            if (obj instanceof CborNumber) {
                sum += ((CborNumber) obj).floatValue();
            }
        }

        // Prints out `Sum: 5.0`
        System.out.println("Sum: " + sum);
    }

    @Test
    public void testGoogleCBORCreate() throws CborParseException, CborConversionException {
        CborMap map = CborMap.create();
        map.put("name", CborTextString.create("zhumingwu"));
        map.put("age", CborInteger.create(18));
        map.put("types", CborArray.createFromJavaObject(new String[]{"男", "富", "帅"}));
        System.out.println("toString: " + map.toString().getBytes(StandardCharsets.UTF_8).length);
        System.out.println("toByteArray: " + map.toCborByteArray().length);
        var buf = map.toCborByteArray();
        var len = buf.length;
        for (int i = 0; i < len; i++) {
            System.out.println("toCborByteArray: " + buf[i]);
        }
    }

}
