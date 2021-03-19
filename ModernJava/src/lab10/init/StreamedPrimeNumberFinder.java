package lab10.init;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.IntConsumer;
import java.util.function.Predicate;
import java.util.stream.IntStream;

@SuppressWarnings({"SameParameterValue", "Convert2MethodRef", "Java8MapForEach", "SpellCheckingInspection"})
public class StreamedPrimeNumberFinder {

    private static void printPrimes(int start, int end, IntConsumer peeker) {

        // todo Implement this: Change this stream to run in parallel and keep the order
        System.out.println("List of prime numbers (in order): ");
        IntStream.rangeClosed(start, end).
                peek(peeker).
                filter(i -> i > 1).
                filter(i -> IntStream.rangeClosed(2, (int) Math.sqrt(i)).noneMatch(innerI -> i % innerI == 0)).
                forEach(i -> System.out.println(" - " + i));
    }

    public static void main(String [] args) {
        Map<String, Integer> threadCountStore = new ConcurrentHashMap<>();
        IntConsumer threadPeeker = i -> threadCountStore.merge(Thread.currentThread().getName(), 1, (prev, cur) -> prev + cur);
        Predicate<Map<?, ?>> isParallel = map -> map.size() > 1;

        printPrimes(1, 1_000_000, threadPeeker);

        System.out.println("Did it run in parallel: " + isParallel.test(threadCountStore));
        System.out.println("List of threads used in computation: ");
        threadCountStore.entrySet().forEach(e -> System.out.println(" - Thread: " + e.getKey() + ": " + e.getValue()));
    }
}
