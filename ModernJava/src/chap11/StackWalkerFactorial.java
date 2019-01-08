package chap11;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StackWalkerFactorial {

    private static final List<List<java.lang.StackWalker.StackFrame>> frameLogs = new ArrayList<>();

    private static void logFrames(String methodToLog) {
        frameLogs.add(
                StackWalker.getInstance().
                        walk(nextFrame -> nextFrame.filter(
                                filterFrame -> filterFrame.getMethodName().equals(methodToLog)).
                                    collect(Collectors.toList())));
    }

    private static int factorial(int count) {
        if (count == 1) {
            logFrames("factorial");
        }
        return count > 0 ? count * factorial(--count) : 1;
    }

    private static void printStackFrameLog(int factorial, int element) {

        System.out.println("Stack frame log of " + factorial);

        Stream<StackWalker.StackFrame> stream = frameLogs.get(element).stream();
        String stringifiedFrames =
                stream.map(frame -> frame.getLineNumber() + ":" +
                        frame.getMethodName()).reduce("", (l, r) -> l + " -> " + r);

        System.out.println(stringifiedFrames);
    }

    public static void main(String... args) {
        System.out.println("11! = " + factorial(11));
        System.out.println("8! = " + factorial(8));
        System.out.println("5! = " + factorial(5));

        printStackFrameLog(11, 0);
     }
}
