package chap06;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static lab.util.Util.logWithThread;
import static lab.util.Util.doWork;

/**
 * This example shows how 3 worker threads can wait at a rendez-vous point before continuing.
 */
public class CyclicBarrierExample {

	private static final int NUMBER_OF_THREADS = 3;
	
	public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
		
		List<Thread> threads = new ArrayList<Thread>(NUMBER_OF_THREADS);
		CyclicBarrier sharedBarrier = new CyclicBarrier(3);
		int workingSeconds = 0;
		
		for (int index = 0; index < NUMBER_OF_THREADS; ++index) {
			Thread nextThread = new Thread(new Worker(sharedBarrier, ++workingSeconds + 2));
			threads.add(nextThread);
		}
		
		for (Thread nextThread : threads) {
			nextThread.start();
		}
		
		logWithThread("Waiting for all threads to finish...");
		for (Thread nextThread : threads) {
			nextThread.join();
		}
		logWithThread("Waiting for all threads to finish...Done");
	}

	private static class Worker implements Runnable {

		private final CyclicBarrier rendezVous;
		private final long delay;

		public Worker(CyclicBarrier barrier, int delayInSeconds) {
			this.rendezVous = barrier;
			this.delay = delayInSeconds;
		}

		@Override
		public void run() {

			logWithThread("Working...");
			doWork(delay);
			logWithThread("Working...done");

			try {
				logWithThread("Waiting...");
				rendezVous.await();
			} catch (InterruptedException e) {
				logWithThread("Waiting...interrupted");

			} catch (BrokenBarrierException e) {
				logWithThread("Waiting...interrupted");
			}
			logWithThread("Waiting...done");
		}
	}
}
