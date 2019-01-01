package lab06.init;

import java.util.concurrent.ExecutionException;

import lab.util.Util;

public class ThreadedPrimeNumberFinder {

	/**
	 * Returns the number of primes found in the range.
	 * 
	 * @param range The range for which to count primes.
	 * @return The number of primes found.
	 * @throws InterruptedException Not thrown in this initial implementation but will be required for threading.
	 * @throws ExecutionException Not thrown in this initial implementation but will be required for threading.
	 */
	private static int countPrimes(int range) throws InterruptedException, ExecutionException {

		// Make this multi-threaded using the executor service.
		// Change this to executor service with callback and futures.
		// 1) Choose the appropriate executor service and mind the pool size
		// 2) Use submit/invoke, call and future, then shutdown the pool.
		
		// Objective:
		// T1 -- countPrimeInRange --
		// T2 -- countPrimeInRange --\
		// T3 -- countPrimeInRange ---> sum
		// T4 -- countPrimeInRange --/
		// T5 -- countPrimeInRange --
		int totalPrimesFound = 0;
		
		for (int index = 0; index < range; ++index) {
			totalPrimesFound = Util.isPrime(index) ? totalPrimesFound + 1 : totalPrimesFound;
		}
		
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
