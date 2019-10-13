package chap12;


import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class StackInstrumentationDemo {

    private static void walkingTest() {
        System.out.println("Walking test");
        List<StackWalker.StackFrame> stack = StackWalker.getInstance().walk(s -> s.limit(10).collect(Collectors.toList()));
        stack.forEach(System.out::println);
    }

    public static void main(String... args) {
        walkingTest();

        StackWalkerInstrumenter.initialize(ClassC.class);
        ClassA.methodA(5);
        ClassB.methodB(5);
        ClassC.methodC(25);

        ClassA.methodA(6);
        ClassB.methodB(6);
        ClassC.methodC(36);

        System.out.println("Instrumentation results: ");
        Consumer<StackWalker.StackFrame> consumer =
                next -> System.out.println(" - " + next.getClassName() + ":" + next.getMethodName() + " (" + next.getLineNumber() + ")");
        StackWalkerInstrumenter.getInstance().processStackFramesWithClass(consumer);
     }

     private static class ClassA {

        private static void methodA(int number) {
            System.out.println("Factorial of " + number + " is " + InstrumentedMath.factorial(number));
        }
     }

    private static class ClassB {

        private static void methodB(int number) {
            System.out.println("Square of " + number + " is " + InstrumentedMath.square(number));
        }
    }

    private static class ClassC {

        private static void methodC(int number) {
            System.out.println("Square root of " + number + " is " + InstrumentedMath.squareRoot(number));
        }
    }
}
