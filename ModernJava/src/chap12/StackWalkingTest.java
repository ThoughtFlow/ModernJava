package chap12;


import java.util.List;
import java.util.stream.Collectors;

public class StackWalkingTest {

    private static void walkingTest() {
        System.out.println("Walking test");
        List<StackWalker.StackFrame> stack = StackWalker.getInstance().walk(s ->
                s.limit(10).collect(Collectors.toList()));
        stack.forEach(System.out::println);
    }

    public static void main(String... args) {
        walkingTest();
     }
}
