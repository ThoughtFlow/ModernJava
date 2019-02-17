package chap06;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import static lab.util.Util.logWithThread;
import static lab.util.Util.doWork;

/**
 * This example shows how semaphores can be used to abstract licenses. Each worker thread must first obtain a license
 * before continuing and must wait if there are no more license remaining.
 */
public class SemaphoreExample {

	private static final int NUMBER_OF_THREADS = 5;
	
	public static void main(String[] args) throws InterruptedException {
		
		List<Thread> threads = new ArrayList<Thread>(NUMBER_OF_THREADS);
		Semaphore semaphore = new Semaphore(NUMBER_OF_THREADS / 2);
		
		for (int index = 0; index < NUMBER_OF_THREADS; ++index) {
			Thread nextThread = new Thread(new Worker(semaphore, (index * 2 + 1)));
			threads.add(nextThread);
		}
		
		for (Thread nextThread : threads) {
			nextThread.start();
		}
		
		for (Thread nextThread : threads) {
			nextThread.join();
		}
	}

	private static class Worker implements Runnable {

		private final Semaphore semaphore;
		private final long delay;

		public Worker(Semaphore semaphore, long delay) {
			this.semaphore = semaphore;
			this.delay = delay;
		}

		@Override
		public void run() {

			for (int index = 0; index < 3; ++ index) {
			   try {
				   logWithThread("Trying to acquire...");
				   semaphore.acquire();
				   logWithThread("Trying to acquire...acquired");
				   doWork(delay);
				   semaphore.release();
				   logWithThread("Trying to acquire...released");
			   } catch (InterruptedException e) {
			       // Do something with exception
			   }
			}
		}
	}
}
