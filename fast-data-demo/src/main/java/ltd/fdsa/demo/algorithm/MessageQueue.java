package ltd.fdsa.demo.algorithm;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.util.StringUtils;

/*
 *  Breadth-First Search 
 */
public class MessageQueue {

	private static final int queueSize = 10;
	private PriorityQueue<Integer> queue = new PriorityQueue<Integer>(queueSize);
	private Lock lock = new ReentrantLock();
	private Condition empty = lock.newCondition();
	public void demo(String[] args) {

		Consumer consumer = new Consumer();
		consumer.start();

		Producer producer = new Producer();

		Scanner input = new Scanner(System.in);
		while (true) {
			System.out.println("请输入");
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
					e.printStackTrace();
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