package lab08.fin;

import lab.util.Util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class ThreadedPrimeNumberFinder {

	private static class PrimeFinderTask extends RecursiveTask<Integer> {

		private static final int OPTIMAL_RANGE = 1000;

		private final int startRange;
		private final int endRange;

		PrimeFinderTask(int startRange, int endRange) {
			this.startRange = startRange;
			this.endRange = endRange;
		}

		@Override
		public Integer compute() {
			int count = 0;
			int range = endRange - startRange;

			if (range > OPTIMAL_RANGE) {
			    int halfWay = range / 2 + startRange;
				PrimeFinderTask firstHalf = new PrimeFinderTask(startRange, halfWay);
				firstHalf.fork();
				PrimeFinderTask secondHalf = new PrimeFinderTask(halfWay + 1, endRange);
				secondHalf.fork();
				count = firstHalf.join() + secondHalf.join();
			}
			else {
				for (int primeCandidate = startRange; primeCandidate <= endRange; ++primeCandidate) {
					count += Util.isPrime(primeCandidate) ? 1 : 0;
				}
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
		ForkJoinPool executor = (ForkJoinPool) Executors.newWorkStealingPool();
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