package lab20.fin;

import java.io.IOException;

/**
 * This is a customized version of the BiConsumer functional interface with the added IOException.
 *
 * @param <T> Parameterized type for the first input to function
 * @param <U> Parameterized type for the second input of function
 */
@FunctionalInterface
public interface IoBiConsumer<T, U> {

    void accept(T t, U u) throws IOException;
}
