package cn.zhumingwu.research.base;

import lombok.extern.slf4j.Slf4j;


import org.springframework.test.context.junit4.SpringRunner;


/**
 * @author zhumingwu
 * @ClassName:
 * @description:
 * @since 2020-10-28
 */

@Slf4j
public class TestJavaFunc {
    @FunctionalInterface
    interface SFunc<T> {
        String[] apply(T var1);
    }

    @Test
    public void Profit() {

    }
}
