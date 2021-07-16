package lab20.fin;

import java.io.IOException;

@FunctionalInterface
public interface IoFunction<T, U> {

    U apply(T t) throws IOException;
}
