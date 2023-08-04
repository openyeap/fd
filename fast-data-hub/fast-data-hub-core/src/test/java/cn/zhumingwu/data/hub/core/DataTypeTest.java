package cn.zhumingwu.data.hub.core;

import lombok.extern.slf4j.Slf4j;



import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ConcurrentSkipListSet;

@Slf4j
public class DataTypeTest {

    @Test
    public void testSortedLong() {
//        var list = new TreeSet<Long>();
        var list = new ConcurrentSkipListSet<Long>();
        Random r = new Random();

        for (int i = 0; i < 100; i++) {
            list.add(r.nextLong());

        }
        System.out.println(list.first());
        Iterator<Long> iterator = list.iterator();
        //迭代器便利
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    @Test
    public void TestData() {
        Double d = 3.15D;
        var l = Double.doubleToLongBits(d);
        System.out.println(l);
    }

    @Test
    public void TestBoolData() {
        Float f = 3.14f;

        System.out.println(f.toString());
        var i = Float.floatToIntBits(f);
        System.out.println(i);

    }

}
