package chap12;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StackWalkerInstrumenter {

    private static StackWalkerInstrumenter singleInstance;

    private final List<List<StackWalker.StackFrame>> stackFrameStore = new ArrayList<>();
    private final String instrumentationClassName;

    public static void initialize(Class<?> instrumentationClass) {
        if (singleInstance != null) {
            throw new IllegalStateException("Instance already created");
        }

        singleInstance = new StackWalkerInstrumenter(instrumentationClass);
    }

    public static StackWalkerInstrumenter getInstance() {
        if (singleInstance == null) {
            throw new IllegalStateException("Instance not initialized");
        }

        return singleInstance;
    }

    private StackWalkerInstrumenter(Class<?> instrumentationClass) {
        instrumentationClassName = instrumentationClass.getName();
    }

    public void captureStackFrame() {
        // Check first if the stack trace contains the class we're looking for
        if (StackWalker.getInstance().walk(nextFrame -> nextFrame.anyMatch(frame -> frame.getClassName().equals(instrumentationClassName)))) {

            // Capture all the stack frame except for this method itself.
            stackFrameStore.add(StackWalker.getInstance().
                    walk(nextFrame -> nextFrame.filter(frame -> !frame.getMethodName().equals("captureStackFrame")).
                            collect(Collectors.toList())));
        }
    }

    public void processStackFramesWithClass(Consumer<StackWalker.StackFrame> consumer) {
        stackFrameStore.
           forEach(next -> {
                     System.out.println("Next occurrence: ");
                     IntStream.iterate(next.size() -1, index -> index >= 0, index -> --index).forEach(index -> consumer.accept(next.get(index)));
                  });
    }
}
