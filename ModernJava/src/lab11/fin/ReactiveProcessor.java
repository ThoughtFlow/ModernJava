package lab11.fin;

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

public class ReactiveProcessor {

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
                             2, 		                                    // Queue size is set to two
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
            Function<Flow.Subscriber<Integer>, List<Integer>> eventPublisher = ReactiveProcessor::publishEvents;
            MySubscriber subscriber = new MySubscriber();

            List<Integer> droppedNumbers = eventPublisher.apply(subscriber);
            List<Integer> processedNumbers = subscriber.getNumbersProcessed();
            verify(droppedNumbers, processedNumbers);
        }

        // Do with error
        {
            Function<Flow.Subscriber<Integer>, List<Integer>> eventPublisher = ReactiveProcessor::publishEventsWithError;
            MySubscriber subscriber = new MySubscriber();

            List<Integer> droppedNmbers = eventPublisher.apply(subscriber);
            List<Integer> processedNumbers = subscriber.getNumbersProcessed();
            verify(droppedNmbers, processedNumbers);
        }
    }

    /**
     * A flow subscriber implementation.
     */
    private static class MySubscriber implements Flow.Subscriber<Integer> {

        private Flow.Subscription subscription;
        private List<Integer> numbersProcessed = new ArrayList<>();

        /**
         * Returns the list of numbers processed.
         *
         * @return The list.
         */
        public List<Integer> getNumbersProcessed() {
            return numbersProcessed;
        }

        /**
         * Callback method used to signal that this subscriber has now subscribed to the publisher.
         *
         * @param subscription The subscription object from which to request more messages.
         */
        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            subscription.request(1);
            this.subscription = subscription;
        }

        /**
         * The callback method used to receive messages.
         *
         * @param nextNumber The next number to received.
         */
        @Override
        public void onNext(Integer nextNumber) {
            try {
                simulateWork(nextNumber);
                numbersProcessed.add(nextNumber);
            }
            catch (RuntimeException exception) {
                subscription.cancel();
            }

            subscription.request(1);
        }

        /**
         * The callback method used to signal that either the publisher or subscriber has errored out. No more messages
         * will be received.
         *
         * @param throwable The exception that triggered the error.
         */
        @Override
        public void onError(Throwable throwable) {
            logWithThread("Received error: " + throwable.getMessage());
        }

        /**
         * Callback method used to signal that no more messages will be received.
         */
        @Override
        public void onComplete() {
            logWithThread("Done");
        }
    }
}
