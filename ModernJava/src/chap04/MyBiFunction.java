package chap04;

import java.util.function.IntFunction;

@FunctionalInterface
public interface MyBiFunction {
    IntFunction<Integer> apply(int a, int b);
}