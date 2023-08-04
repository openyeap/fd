package cn.zhumingwu.data.hub.core;

import cn.zhumingwu.data.hub.core.util.CRCUtil;
import lombok.extern.slf4j.Slf4j;



import java.util.*;

@Slf4j
public class CRCUtilTest {

    @Test
    public void testPrint() {
        var bytes = "currentTimeMillis".getBytes();
        var crc = CRCUtil.crc32(new byte[0]);

        crc.update(Arrays.copyOfRange(bytes, 0, 12));
        var mm = crc.getValue();
        crc = CRCUtil.crc32(new byte[0]);
        crc.update((int) mm);
        crc.update(Arrays.copyOfRange(bytes, 12, 17));
        var b = crc.getValue();
        System.out.println(b);
        var r = CRCUtil.crc32(bytes).check(b);
        System.out.println(r);
    }
}
