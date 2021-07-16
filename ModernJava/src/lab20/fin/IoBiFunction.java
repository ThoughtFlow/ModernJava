package lab20.fin;

import java.io.IOException;

@FunctionalInterface
public interface IoBiFunction<T, U, R> {

    R apply(T t, U u) throws IOException;
}
