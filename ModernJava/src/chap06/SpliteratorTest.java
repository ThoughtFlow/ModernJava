package chap06;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;

public class SpliteratorTest {

    private static void spliterator() {
         List<Integer> integerList = new ArrayList<>();
         for (int index = 0; index < 1000; ++index) {
             integerList.add(index);
         }

        // Split in 4 equal sizes
        // Careful - the trySplit may not work and return null
        Spliterator<Integer> spliterator1 = integerList.spliterator();
        Spliterator<Integer> spliterator2 = spliterator1.trySplit();
        Spliterator<Integer> spliterator3 = spliterator1 != null ? spliterator1.trySplit() : null;
        Spliterator<Integer> spliterator4 = spliterator2 != null ? spliterator2.trySplit() : null;

        System.out.println("Spliterator 1");
        spliterator1.forEachRemaining(System.out::println);

        System.out.println("Spliterator 2");
        spliterator2.forEachRemaining(System.out::println);

        System.out.println("Spliterator 3");
        spliterator3.forEachRemaining(System.out::println);

        System.out.println("Spliterator 4");
        spliterator4.forEachRemaining(System.out::println);
    }

    public static void main(String... args) {
        spliterator();
    }
}
