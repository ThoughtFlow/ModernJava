package lab10.fin;

import lab.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@SuppressWarnings({"ConstantConditions", "UnnecessaryLocalVariable", "SpellCheckingInspection"})
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
        }
        catch (InterruptedException | ExecutionException exception) {
            totalPrimesFound = 0;
        }
        finally {
            pool.shutdown();
        }

        return totalPrimesFound;
    }

    public static void main(String[] args)  {

        Function<List<Integer>, List<Spliterator<Integer>>> spliteratorCreator = integerList -> {

            // Split in 4 equal sizes
            // Careful - the trySplit may not work and return null
            Spliterator<Integer> spliterator1 = integerList.spliterator();
            Spliterator<Integer> spliterator2 = spliterator1.trySplit();
            Spliterator<Integer> spliterator3 = spliterator1 != null ? spliterator1.trySplit() : null;
            Spliterator<Integer> spliterator4 = spliterator2 != null ? spliterator2.trySplit() : null;

            List<Spliterator<Integer>> spliterators =
                    Arrays.asList(spliterator1,
                                  spliterator2,
                                  spliterator3,
                                  spliterator4);

            return spliterators;
        };

        Function<Spliterator<Integer>, Integer> primeCounter = spliterator -> {
            AtomicInteger counter = new AtomicInteger(0);

            // Make sure the spliterator is not null
            if (spliterator != null) {

                spliterator.forEachRemaining(nextInt ->
                        counter.updateAndGet(containedInt -> Util.isPrime(nextInt) ?
                                ++containedInt : containedInt));
            }

            return counter.get();
        };

        System.out.println("Total primes found: " + execute(spliteratorCreator, primeCounter));
    }
}
