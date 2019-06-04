package chap05;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings({"SimplifyStreamApiCallChains", "Java8MapForEach"})
public class StreamCollectors {

    private static List<String> testToCollection(List<String> data) {
        return data.stream().collect(Collectors.toCollection(ArrayList<String>::new));
    }

    private static double testAverage(List<String> data) {
        // Find the average length of each string
        return data.stream().collect(Collectors.averagingInt(String::length));
    }

    private static int testAverageWithFinisher(List<String> data) {
        // Find the average length of each string but apply a finisher to convert to an int
        return data.stream().collect(Collectors.collectingAndThen(Collectors.averagingInt(String::length), Double::intValue));
    }

    private static double testAverageWithFiltering(List<String> data) {
        return data.stream().collect(Collectors.filtering(s -> s.contains("Java"), Collectors.averagingInt(String::length)));
    }

    private static double testCounting(List<String> data) {
        return data.stream().collect(Collectors.counting());
    }

    private static double testFlatMapping(List<String> data) {
        return data.stream().collect(Collectors.flatMapping(s -> Stream.of(s, s), Collectors.counting()));
    }

    private static Map<Integer, List<String>> testGroupingBy(List<String> data) {
        // Group the strings inside a map where the key is the string length
        return data.stream().collect(Collectors.groupingBy(String::length));
    }

    private static String testJoining(List<String> data) {
        // Join the list of strings into a giant string delimited by a comma
        return data.stream().collect(Collectors.joining(", "));
    }

    private static String testMapping(List<String> data) {
        // Join the list of strings into a giant string delimited by a comma but apply a function that truncates to the first five characters
        return data.stream().collect(Collectors.mapping(s -> s.length() > 5 ? s.substring(0, 5) : s, Collectors.joining()));
    }

    private static Map<Integer, List<String>> testToMap(List<String> data) {
        // Map the list of strings by their size
        Function<String, Integer> keyMapper = String::length;
        Function<String, List<String>> valueMapper = s -> {
            List<String> collection = new ArrayList<>();
            collection.add(s);
            return collection;
        };
        BinaryOperator<List<String>> mergeFunction = (list1, list2) -> {list1.addAll(list2); return list1;};
        return data.stream().collect(Collectors.toMap(keyMapper, valueMapper, mergeFunction));
    }

    private static Map<Boolean, List<String>> testPartitioning(List<String> data) {
        // Partition the list of strings inside a map where the key is a boolean determining whether or not the string contains the "Java" word
        return data.stream().collect(Collectors.partitioningBy(s -> s.contains("Java")));
    }

    private static List<String> testTeeing(List<String> data) {
        // Teeing function will send the list to the two filtering collectors then merge the results
        Collector<String, ?, List<String>> filteringJavaCollector = Collectors.filtering(s -> s.contains("Java"), Collectors.toList());
        Collector<String, ?, List<String>> filteringOracleCollector = Collectors.filtering(s -> s.contains("Oracle"), Collectors.toList());
        BinaryOperator<List<String>> unique = (list1, list2) -> {
            list1.addAll(list2);
            return list1.stream().distinct().collect(Collectors.toList());
        };

        return data.stream().collect(Collectors.teeing(filteringJavaCollector, filteringOracleCollector, unique));
    }

    public static void main(String... args) {
        List<String> list = Arrays.asList("Short string",
                                          "Slightly longer string with numbers 123",
                                          "Java 12, released in March 2019, is the latest version of Java",
                                          "OpenJDK is the open source version of Java",
                                          "Starting from version 11, Oracle has changed the licensing for Java - it will no longer be free");

        System.out.println(testToCollection(list));
        System.out.println(testAverage(list));
        System.out.println(testAverageWithFinisher(list));
        System.out.println(testAverageWithFiltering(list));
        System.out.println(testCounting(list));
        System.out.println(testFlatMapping(list));

        testGroupingBy(list).entrySet().forEach(e -> System.out.println("Key: " + e.getKey() + " Value: " + e.getValue()));

        System.out.println(testJoining(list));
        System.out.println(testMapping(list));
        testToMap(list).entrySet().forEach(e -> System.out.println("Key: " + e.getKey() + " Value: " + e.getValue()));
        testPartitioning(list).entrySet().forEach(e -> System.out.println("Key: " + e.getKey() + " Value: " + e.getValue()));
        System.out.println(testTeeing(list));
    }
}
