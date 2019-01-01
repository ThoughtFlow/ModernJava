package lab08.init;

import lab.util.Util;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;


public class ThreadedPrimeNumberCollector {

	private static class PrimeFinderAction extends RecursiveAction {

		private final int startRange;
		private final int endRange;
		private final boolean[] collector;

		PrimeFinderAction(int startRange, int endRange, boolean[] collector) {
			this.startRange = startRange;
			this.endRange = endRange;
			this.collector = collector;
		}

        @Override
        public void compute() {

            // Refactor this method so that the work is divided into smaller chunks of 1000 elements.
            // Use fork and join
            for (int primeIndex = startRange; primeIndex <= endRange; ++primeIndex) {
                collector[primeIndex] = Util.isPrime(primeIndex);
            }
        }
	}
	
	/**
	 * Fork-join based prime counter. Splits the range into 1000 number ranges and returns the count.
	 * 
	 * @param range The range for which to count primes.
	 * @return The number of primes found.
	 */
	private static boolean[] getPrimes(int range) throws InterruptedException, ExecutionException {
		ForkJoinPool executor = (ForkJoinPool) Executors.newWorkStealingPool();
		boolean[] primeCollection = new boolean[range + 1];
		Arrays.fill(primeCollection, false);

		ForkJoinTask<Void> completion = executor.submit(new PrimeFinderAction(1, range, primeCollection));
		completion.get();
		executor.shutdown();

		return primeCollection;
	}

	public static void main(String[] args) {
		try {
		    int count = 0;
            boolean[] primeCollection = getPrimes(1_000_000);
            System.out.println("Primes found: ");

            for (int index = 0; index < primeCollection.length; ++index) {
                if (primeCollection[index]) {
                    ++count;
                    System.out.println(" - " + index);
                }
            }
            System.out.println("Count: " + count);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
}