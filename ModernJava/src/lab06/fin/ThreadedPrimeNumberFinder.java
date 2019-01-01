package lab06.fin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import lab.util.Util;

public class ThreadedPrimeNumberFinder {

	private static class PrimeFinder implements Callable<Integer> {

		private final int startRange;
		private final int endRange;

		public PrimeFinder(int startRange, int endRange) {
			this.startRange = startRange;
			this.endRange = endRange;
		}

		@Override
		public Integer call() {
			int primesFound = 0;
			
			for (int primeCandidate = startRange; primeCandidate <= endRange; ++primeCandidate) {
				primesFound += Util.isPrime(primeCandidate) ? 1 : 0;
			}

			return primesFound;
		}
	}
	
	/**
	 * Multi-threaded prime counter. Splits the range into 1000 number ranges and returns the count.
	 * 
	 * @param range The range for which to count primes.
	 * @return The number of primes found.
	 * @throws InterruptedException Thrown upon a thread error.
	 * @throws ExecutionException Thrown upon an error in the callable.
	 */
	private static int countPrimes(int range) throws InterruptedException, ExecutionException {

		final int intsPerSlice = 1000;
		final int slices = range / intsPerSlice;

		final List<PrimeFinder> primeFinders = new ArrayList<>(slices);
		final ExecutorService pool = Executors.newFixedThreadPool(10);

		int totalPrimesFound = 0;

		for (int index = 0; index < slices; ++index) {
			primeFinders.add(new PrimeFinder(index * intsPerSlice, index * intsPerSlice + intsPerSlice - 1));
		}
		
		List<Future<Integer>> futures = pool.invokeAll(primeFinders);

		for (Future<Integer> nextFuture : futures) {
			totalPrimesFound += nextFuture.get();
		}

		pool.shutdown();
		
		return totalPrimesFound;
	}
	
	public static void main(String[] args) {
		try {
			System.out.println("Total primes found: " + countPrimes(1000000));
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
}