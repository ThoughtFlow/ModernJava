package lab09.init;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class PromisesPrimeNumberFinder {

	/**
	 * Returns a set of chained promises as one. Each promise individually counts the number of primes in its range. 
	 *  
	 * @param numberOfRanges Number of slices.
	 * @param rangeSize Size of each slice.
	 * @return A promise that is ready to be executed.
	 */
	private static CompletableFuture<Integer> createPromise(int numberOfRanges, int rangeSize) {
		// Implement this
		return CompletableFuture.completedFuture(0);
	}
	
	/**
	 * Returns a set of chained promises as one. Each promise individually counts the number of primes in its range. Defaults to zero any promise that errors out.
	 *  
	 * @param numberOfRanges Number of slices.
	 * @param rangeSize Size of each slice.
	 * @return A promise that is ready to be executed.
	 */
	private static CompletableFuture<Integer> createPromiseWithException(int numberOfRanges, int rangeSize) {
		// Implement this
		return CompletableFuture.completedFuture(0);
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		System.out.println("Total primes found: " + createPromise(1000, 1000).get());
		System.out.println("Total primes found: " + createPromiseWithException(1000, 1000).get());
	}
}
