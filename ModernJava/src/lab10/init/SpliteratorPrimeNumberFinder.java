package lab10.init;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

@SuppressWarnings({"SpellCheckingInspection"})
public class SpliteratorPrimeNumberFinder {

    private static final int LIST_SIZE = 1000000;

    private static int countPrimes(List<Callable<Integer>> primeFinders, ExecutorService pool)
            throws InterruptedException, ExecutionException {

        List<Future<Integer>> futures = pool.invokeAll(primeFinders);
        int totalPrimesFound = 0;
        for (Future<Integer> nextFuture : futures) {
            totalPrimesFound += nextFuture.get();
        }

        return totalPrimesFound;
    }

    private static int execute(Function<List<Integer>, List<Spliterator<Integer>>> spliteratorCreator,
                               Function<Spliterator<Integer>, Integer> primeCounter) {
        int totalPrimesFound;
        ExecutorService pool = Executors.newFixedThreadPool(4);

        // Populate the list of integers
        List<Integer> integerList = new ArrayList<>();
        for (int index = 0; index < LIST_SIZE; ++index) {
            integerList.add(index);
        }

        List<Callable<Integer>> primeFinders = new ArrayList<>();

        for (Spliterator<Integer> next : spliteratorCreator.apply(integerList)) {
            Callable<Integer> callable = () -> primeCounter.apply(next);
            Callable<Integer> printThread = () -> {
                System.out.println(Thread.currentThread().getName() + ": " + next.getExactSizeIfKnown());
                return callable.call();
            };
            primeFinders.add(printThread);
        }

        try {
            totalPrimesFound = countPrimes(primeFinders, pool);
        } catch (InterruptedException | ExecutionException exception) {
            totalPrimesFound = 0;
        } finally {
            pool.shutdown();
        }

        return totalPrimesFound;
    }

    public static void main(String[] args) {

        // todo Implement this function that returns a list of Spliterators for Integers
        // Input: The list of integers to test for primeness
        // Output: The list of spliterators of integers representing the range to process
        Function<List<Integer>, List<Spliterator<Integer>>> spliteratorCreator = integerList -> {

            return new ArrayList<Spliterator<Integer>>();
        };

        // todo Implement this function that returns the number of primes found in the spliterator data
        // Input: The spliterator of integers to process
        // Output: The number of primes found in the spliterator
        Function<Spliterator<Integer>, Integer> primeCounter = spliterator -> {
            return 0;
        };

        System.out.println("Total primes found: " + execute(spliteratorCreator, primeCounter));
    }
}