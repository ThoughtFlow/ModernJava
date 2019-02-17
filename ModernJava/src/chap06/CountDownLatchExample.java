package chap06;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static lab.util.Util.logWithThread;
import static lab.util.Util.doWork;

/**
 * This example shows how a master thread can wait for 3 worker threads to each complete their work until the master
 * thread continues.
 */
public class CountDownLatchExample {

	private static final int NUMBER_OF_THREADS = 3;
	
	public static void main(String[] args) throws InterruptedException {
		
		List<Thread> threads = new ArrayList<Thread>(NUMBER_OF_THREADS);
		CountDownLatch latch = new CountDownLatch(3);
		int workingSeconds = 0;
		
		for (int index = 0; index < NUMBER_OF_THREADS; ++index) {
			Thread nextThread = new Thread(new Worker(latch, ++workingSeconds + 2));
			threads.add(nextThread);
		}
		
		for (Thread nextThread : threads) {
			nextThread.start();
		}

		logWithThread("Waiting for all threads to finish...");
		latch.await();
		logWithThread("Waiting for all threads to finish...Done");
	}

	private static class Worker implements Runnable {

		private final CountDownLatch latch;
		private final long delay;

		public Worker(CountDownLatch latch, int delayInSeconds) {
			this.latch = latch;
			this.delay = delayInSeconds;
		}

		@Override
		public void run() {

			logWithThread("Working...");
			doWork(delay);
			logWithThread("Working...done");

			latch.countDown();
		}
	}
}
