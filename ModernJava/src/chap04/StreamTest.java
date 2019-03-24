package chap04;

import java.util.List;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StreamTest {

    private static void serialPrimeCounter() {
        IntPredicate isNotOne = i -> i != 1;
        IntPredicate isIndivisible = i -> IntStream.rangeClosed(2, (int) Math.sqrt(i)).noneMatch(innerI -> i % innerI == 0);
        IntPredicate isPrime = isNotOne.and(isIndivisible);

        List<Integer> primes = IntStream.range(1, 100000).filter(isPrime).boxed().collect(Collectors.toList());
        System.out.println(primes.size());

    }

    private static void parallelPrimeCounter() {
        IntPredicate isNotOne = i -> i != 1;
        IntPredicate isIndivisible = i -> IntStream.rangeClosed(2, (int) Math.sqrt(i)).noneMatch(innerI -> i % innerI == 0);
        IntPredicate isPrime = isNotOne.and(isIndivisible);
        
        List<Integer> primes = IntStream.range(1, 100000).parallel().filter(isPrime).boxed().collect(Collectors.toList());
        System.out.println(primes.size());
    }

    public static void main(String... args) {
        serialPrimeCounter();
        parallelPrimeCounter();
    }
}
