package chap12;

import io.reactivex.annotations.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * Created by Nick Maiorano.
 */
public class VarTests {

    private static void testType(long type) {
        System.out.println(type + " is a long");
    }

    private static void testType(int type) {
        System.out.println(type + " is an int");
    }

    private static void testType(Object type) {
        System.out.println(type + " is of type: " + type.getClass());
    }

    private static void testVars() {
        int localVar9 = 9; // Pre-Java 10
        var localVar10 = 10; // Java 10
        var list = new ArrayList<String>();
        var list2 = Arrays.asList("Hello", "There");

        // Doesn't compile - Non-denotable
//        var x;
//        var f = () -> { };
//        var g = null;

        int var = 10; // Compiles!
    }

    private static void testInferredTypes() {
        var anInt = 10;
        var aLong = 10L;
        var list = new ArrayList<String>();
        var list2 = Arrays.asList("Hello", "There");

        testType(anInt);
        testType(aLong);
        testType(list);
        testType(list2);
    }

    private static void lambdaTypeInference() {
        // Type of S is inferred
        // Var must be enclosed in parentheses
        Consumer<String> consumer = (var s) -> s.intern();

        // Once var is used, all parameters must use var
        BiFunction<Integer, Integer, String> function = (var i, var j) -> Integer.toString(i + j);

        // Does not compile
//        function = (var i, j) -> Integer.toString(i + j);
//        consumer = var s -> s.intern();
    }

    private static void lambdaTypeInference2() {
        // Must use var for parameter annotations
        Consumer<String> consumer = (@NonNull var s) -> s.intern();
    }

    public static void main(String...args) {
        testVars();
        testInferredTypes();
        lambdaTypeInference();
        lambdaTypeInference2();
    }
}
