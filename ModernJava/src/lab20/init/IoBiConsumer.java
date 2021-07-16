package lab20.init;

import java.io.IOException;

@FunctionalInterface
public interface IoBiConsumer<T, U> {

    void accept(T t, U u) throws IOException;
}
