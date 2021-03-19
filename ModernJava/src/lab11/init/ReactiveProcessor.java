package lab11.init;

import java.util.List;
import java.util.concurrent.Flow;
import java.util.function.Function;

import static lab.util.Util.sleep;

public class ReactiveProcessor {

    /**
     * Verifies that the numbers in the dropped list do not occur in the processed list.
     *
     * @param dropped The list of dropped integers to check.
     * @param processed The list of processed integers to check.
     */
    private static void verify(List<Integer> dropped, List<Integer> processed) {
        System.out.println("Dropped: " + dropped.size());
        System.out.println("Processed: " + processed.size());
        System.out.println("Passed: " +
                dropped.stream().allMatch(droppedInt -> processed.stream().noneMatch(droppedInt::equals)));
    }

    /**
     * Simulates processing.
     *
     * @param number The number to process.
     * @throws RuntimeException Thrown if the number is less than 0 to simulate a processing error.
     */
    private static void simulateWork(Integer number) throws RuntimeException {
        if (number < 0) {
            throw new RuntimeException("Unexpected error");
        }

        sleep(100);
    }

    public static void main(String[] args) {
        // Do with full range
        {
            // todo Implement this function
            Function<Flow.Subscriber<Integer>, List<Integer>> eventPublisher = null;

            // todo Implement the flow subscriber and replace the null
            List<Integer> droppedNumbers = eventPublisher.apply(null /* ###Implement me###*/);

            // todo Implement a method in subscriber that will return the processed numbers.
            List<Integer> processedNumbers = null; // ###Implement me###

            verify(droppedNumbers, processedNumbers);
        }

        // Do again with errors
        {
            // todo Implement this function
            Function<Flow.Subscriber<Integer>, List<Integer>> eventPublisher = null;

            // todo Implement the flow subscriber and replace the null
            List<Integer> droppedNumbers = eventPublisher.apply(null /* ###Implement me###*/);

            // todo Implement a method in subscriber that will return the processed numbers.
            List<Integer> processedNumbers = null; // ###Implement me###

            verify(droppedNumbers, processedNumbers);
        }
    }
}
