package ltd.fdsa.research.algorithm;

import lombok.extern.slf4j.Slf4j;

import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 *  Breadth-First Search
 */
@Slf4j
public class MessageQueue {

    private static final int queueSize = 10;
    private final PriorityQueue<Integer> queue = new PriorityQueue<Integer>(queueSize);
    private final Lock lock = new ReentrantLock();
    private final Condition empty = lock.newCondition();

    public void demo(String[] args) {

        Consumer consumer = new Consumer();
        consumer.start();

        Producer producer = new Producer();

        Scanner input = new Scanner(System.in);
        while (true) {
            log.info("请输入");
            int m = input.nextInt();
            producer.ProductMessage(m);
        }
        // input.close();
    }

    class Consumer extends Thread {

        @Override
        public void run() {
            consume();
        }

        private void consume() {
            while (true) {
                try {
                    lock.lock();
                    if (queue.size() <= 0) {
                        System.out.printf("empty.await\r");
                        empty.await();
                    }
                    int m = queue.poll();
                    // notFull.signal();
                    System.out.printf("从队列取走:%d，队列剩余%d\r", m, queue.size());
                    Thread.sleep(6000);
                } catch (Exception e) {
                    log.error("Exception", e);
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    class Producer {
        public void ProductMessage(Integer e) {
            try {
                lock.lock();
                if (e == 0) {
                    empty.signal();
                }
                queue.offer(e);
                System.out.printf("向队列取中插入一个元素%d，队列剩余空间：%d\r", e, queue.size());

            } finally {
                lock.unlock();
            }
        }
    }
}
