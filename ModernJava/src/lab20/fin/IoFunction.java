package lab20.fin;

import java.io.IOException;

/**
 * This is a customized version of the Function functional interface with the added IOException.
 *
 * @param <T> Parameterized type for the input to function
 * @param <U> Parameterized type for the output of function
 */
@FunctionalInterface
public interface IoFunction<T, U> {

    U apply(T t) throws IOException;
}
