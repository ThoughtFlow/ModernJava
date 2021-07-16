package lab20.init;

import java.io.IOException;

@FunctionalInterface
public interface IoFunction<T, U> {

    U apply(T t) throws IOException;
}
