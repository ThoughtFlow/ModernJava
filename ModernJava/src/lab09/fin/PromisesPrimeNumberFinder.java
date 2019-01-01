package lab09.fin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import lab.util.Util;

public class PromisesPrimeNumberFinder {
	
	/**
	 * Returns a set of chained promises as one. Each promise individually counts the number of primes in its range. 
	 *  
	 * @param numberOfRanges Number of slices.
	 * @param rangeSize Size of each slice.
	 * @return A promise that is ready to be executed.
	 */
	private static CompletableFuture<Integer> createPromise(int numberOfRanges, int rangeSize) {
		CompletableFuture<Integer> promise = CompletableFuture.completedFuture(0);

		for (int index = 0; index < numberOfRanges; ++index) {
			
			// Have to copy index to range to make it effectively final - otherwise can't use within the lambda
			int range = index;
			
			CompletableFuture<Integer> nextPromise = CompletableFuture.supplyAsync(() -> Util.countPrimes(range * rangeSize, range * rangeSize + rangeSize - 1));
			promise = promise.thenCombine(nextPromise, (first, second) -> first + second);
		}
		
		return promise;
	}
	
	/**
	 * Returns a set of chained promises as one. Each promise individually counts the number of primes in its range. Defaults to zero any promise that errors out.
	 *  
	 * @param numberOfRanges Number of slices.
	 * @param rangeSize Size of each slice.
	 * @return A promise that is ready to be executed.
	 */
	private static CompletableFuture<Integer> createPromiseWithException(int numberOfRanges, int rangeSize) {
		CompletableFuture<Integer> promise = CompletableFuture.completedFuture(0);

		// Provoke an exception by starting at -1
		for (int index = -1; index < numberOfRanges; ++index) {
			
			// Have to copy index to range to make it effectively final - otherwise can't use within the lambda
			int range = index;

			// Now we have an exceptionally clause - no more exception
			CompletableFuture<Integer> nextPromise = 
					CompletableFuture.supplyAsync(() -> Util.countPrimes(range * rangeSize, range * rangeSize + rangeSize - 1)).exceptionally(i -> 0);

			 //Having no exceptionally clause will result in an exception being thrown
//			 nextPromise = CompletableFuture.supplyAsync(() -> Util.countPrimes(range * sliceSize, range * sliceSize + sliceSize - 1)).
//			 		whenComplete((i, e) -> {if (e != null) System.err.println("Exception caught - continuing: " + e.getMessage());});

			promise = promise.thenCombine(nextPromise, (first, second) -> first + second);
		}
		
		return promise;
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		System.out.println("Total primes found: " + createPromise(1000, 1000).get());
		System.out.println("Total primes found: " + createPromiseWithException(1000, 1000).get());
	}
}
