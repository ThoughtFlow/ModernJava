package lab08.fin;

import lab.util.Util;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class ForkJoinedPrimeNumberCollector {

    private static class PrimeFinderAction extends RecursiveAction {

        private static final int OPTIMAL_RANGE = 1000;

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
            int range = endRange - startRange;

            if (range > OPTIMAL_RANGE) {
                int halfWay = range / 2 + startRange;
                PrimeFinderAction firstHalf = new PrimeFinderAction(startRange, halfWay, collector);
                firstHalf.fork();
                PrimeFinderAction secondHalf = new PrimeFinderAction(halfWay + 1, endRange, collector);
                secondHalf.fork();
                firstHalf.join();
                secondHalf.join();
            }
            else {
                for (int primeIndex = startRange; primeIndex <= endRange; ++primeIndex) {
                    collector[primeIndex] = Util.isPrime(primeIndex);
                }
            }
        }
    }

    /**
     * Fork-join based prime counter. Splits the range into 1000 number ranges and returns an array of boolean whose
     * value is true of its index is prime and false otherwise.
     *
     * @param range The range for which to count primes.
     * @return A boolean array indicating whether or not its index is prime.
     */
    private static boolean[] getPrimes(int range) throws InterruptedException, ExecutionException {
        ForkJoinPool executor = ForkJoinPool.commonPool();
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