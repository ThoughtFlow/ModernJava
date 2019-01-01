package chap06;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ThreadSafety {

    private final static ThreadLocal<Integer> tLocal = ThreadLocal.withInitial(() -> 1);

    private void threadedMethod(int parameter, Map<Integer, List<String>> mapParameter) {
        int localVariable = parameter + 1; // thread-safe

        parameter++;     // thread-safe
        localVariable++; // thread-safe
        mapParameter.put(10, new ArrayList<String>()); // Not necessarily thread-safe
    }

    private static void threadedMethod() {
        int sum = tLocal.get() + 1; // Thread safe

        tLocal.set(sum);
    }


}
