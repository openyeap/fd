package cn.zhumingwu.starter.jdbc.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;


import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j

//@SpringBootTest
public class MyBatisTests {

    @BeforeClass
    public static void WarmUpTheEngine() {
        System.out.println("@BeforeClass - WarmUpTheEngine");
    }

    static void executeRunnables(final ExecutorService service, List<Runnable> runnables) {

        for (Runnable r : runnables) {
            service.execute(r);
        }
        service.shutdown();
    }

    @Before
    public void setUp() {
        System.out.println("@Before - setUp");
    }

    @After
    public void tearDown() {
        System.out.println("@After - tearDown");

    }

    @Test
    public void concurrenceBetweenTwoUsers() throws InterruptedException {
        List<Runnable> runnables = new ArrayList<Runnable>();
        runnables.add(new MonRunnable());
        runnables.add(new MonRunnable());
        runnables.add(new MonRunnable());
        runnables.add(new MonRunnable());
        ExecutorService execute = Executors.newFixedThreadPool(4);
        executeRunnables(execute, runnables);

        Thread.sleep(3000);
    }

    class MonRunnable implements Runnable {
        @Override
        public void run() {

            try {
                int i = 0;
                while (i++ < 10) {
                    Thread.currentThread().sleep(23);
                    //System.out.println("lol");
                    log.info("thread, code,value,controller_action", Thread.currentThread().getName(), 200 + i, 234 + i, "users#show");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
