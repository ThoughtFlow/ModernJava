package lab11.init;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.IntStream;

import static lab.util.Util.logWithThread;
import static lab.util.Util.sleep;

public class ReactiveProcessorWithPublisher {

    /**
     * Publishes integer 1 to 1000 and returns the numbers that were not processed by the subscriber. Will wait up to
     * 50ms for a subscriber to process the number with a queue size max of 2.
     *
     * @param subscriber The subscriber that will process the numbers.
     * @return A list of integers representing the dropped numbers.
     */
    private static List<Integer> publishEvents(Flow.Subscriber<Integer> subscriber) {
        List<Integer> missedNumbers = new ArrayList<>();

        // SubmissionPublisher is an AutoClosable
        try (SubmissionPublisher<Integer> publisher =
                     new SubmissionPublisher<>(ForkJoinPool.commonPool(),	// Create a publisher using the common ForkJoinPool as the async framework.
                             2, 		                    // Queue size is set to two
                             (analyzer, exception) -> logWithThread("Error: " + exception.getMessage()))) // If any subscribers errors out, print out the exception message
        {
            publisher.subscribe(subscriber);

            BiPredicate<Flow.Subscriber<? super Integer>, Integer> onDrop = (sub, number) -> {missedNumbers.add(number); return false;};
            IntStream.rangeClosed(1, 1000).forEach(event -> {
               publisher.offer(event,				       // Publish this event and let any subscriber process it.
                       50, TimeUnit.MILLISECONDS,  // Wait up to 50 milliseconds for a subscriber to process it or it will be dropped
                               onDrop);                    // This handler determines if the event should be retried or not if dropped
            });
        }

        sleep(10000);
        return missedNumbers;
    }

    /**
     * Provokes an error in the subscriber but generating a negative range.
     *
     * @param subscriber The subscriber that will process the numbers.
     * @return A list of integers representing the dropped numbers.
     */
    private static List<Integer> publishEventsWithError(Flow.Subscriber<Integer> subscriber) {
        List<Integer> missedNumbers = new ArrayList<>();

        // SubmissionPublisher is an AutoClosable
        try (SubmissionPublisher<Integer> publisher =
                     new SubmissionPublisher<>(ForkJoinPool.commonPool(),	// Create a publisher using the common ForkJoinPool as the async framework.
                             2, 		                    // Queue size is set to two
                             (analyzer, exception) -> logWithThread("Error: " + exception.getMessage()))) // If any subscribers errors out, print out the exception message
        {
            publisher.subscribe(subscriber);

            BiPredicate<Flow.Subscriber<? super Integer>, Integer> onDrop = (sub, number) -> {missedNumbers.add(number); return false;};
            IntStream.rangeClosed(-1, 10).forEach(event -> {
                publisher.offer(event,				       // Publish this event and let any subscriber process it.
                                50, TimeUnit.MILLISECONDS, // Wait up to 50 milliseconds for a subscriber to process it or it will be dropped
                                onDrop);                   // This handler determines if the event should be retried or not if dropped
            });
        }

        sleep(10000);
        return missedNumbers;
    }

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
            Function<Flow.Subscriber<Integer>, List<Integer>> eventPublisher = ReactiveProcessorWithPublisher::publishEvents;

            // todo Implement the subscriber
            List<Integer> droppedNumbers = eventPublisher.apply(null /* ###Implement me###*/);

            // todo Implement a method in subscriber that will return the processed numbers.
            List<Integer> processedNumbers = null; // ###Implement me### 

            verify(droppedNumbers, processedNumbers);
        }

        // Repeat with the error publish event
        {
            Function<Flow.Subscriber<Integer>, List<Integer>> eventPublisher = ReactiveProcessorWithPublisher::publishEventsWithError;

            // todo Implement the subscriber
            List<Integer> droppedNumbers = eventPublisher.apply(null /* ###Implement me###*/);

            // todo Implement a method in subscriber that will return the processed numbers.
            List<Integer> processedNumbers = null; // ###Implement me### 

            verify(droppedNumbers, processedNumbers);
        }
    }
}
