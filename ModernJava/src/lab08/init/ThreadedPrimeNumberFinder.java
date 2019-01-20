package lab08.init;

import lab.util.Util;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;


public class ThreadedPrimeNumberFinder {

	private static class PrimeFinderTask extends RecursiveTask<Integer> {

		private final int startRange;
		private final int endRange;

		PrimeFinderTask(int startRange, int endRange) {
			this.startRange = startRange;
			this.endRange = endRange;
		}

		@Override
		public Integer compute() {

            // Refactor this method so that the work is divided into smaller chunks of 1000 elements.
            // Use fork and join
			int count = 0;

			for (int primeCandidate = startRange; primeCandidate <= endRange; ++primeCandidate) {
			    count += Util.isPrime(primeCandidate) ? 1 : 0;
			}

			return count;
		}
	}
	
	/**
	 * Fork-join based prime counter. Splits the range into 1000 number ranges and returns the count.
	 * 
	 * @param range The range for which to count primes.
	 * @return The number of primes found.
	 */
	private static int countPrimes(int range) throws InterruptedException, ExecutionException {
		ForkJoinPool executor = ForkJoinPool.commonPool();
		ForkJoinTask<Integer> totalPrimesFound = executor.submit(new PrimeFinderTask(1, range));
		int found = totalPrimesFound.get();
		executor.shutdown();

		return found;
	}

	public static void main(String[] args) {
		try {
			System.out.println("Total primes found: " + countPrimes(1_000_000));
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
}