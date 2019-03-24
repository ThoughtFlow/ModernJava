package chap08;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class ReactiveStreams {

    public static void main(String... args) {

        {
            // Print Hello World
            System.out.println("Flowable with just");
            Flowable.just("Hello world").subscribe(System.out::println);
            System.out.println("==================");
        }

        {
            // Prints 1, 4, 9, 16, 25, 36, 49, 64, 81, 100
            System.out.println("Flowable range with Blocking subscribe");
            Flowable.range(1, 10).map(v -> v * v)
                    .blockingSubscribe(System.out::println);
            System.out.println("==================");
        }

        {
            // Performs computation on different thread
            System.out.println("Flowable range with Blocking subscribe and inner flowable");
            Flowable.range(1, 10)
                    .flatMap(v ->
                            Flowable.just(v)
                                    .subscribeOn(Schedulers.computation())
                                    .map(w -> w * w)
                    ).blockingSubscribe(System.out::println);
            System.out.println("==================");
        }

        {
            // Hot observable – messages will be dropped
            System.out.println("Hot observable - some messages will be dropped");
            AtomicInteger count = new AtomicInteger(0);

            // Create the message source with backpressure set to drop (if producer is too fast, messages are dropped)
            PublishSubject<Integer> observable = PublishSubject.create();
            observable.toFlowable(BackpressureStrategy.DROP).
                    observeOn(Schedulers.computation()).subscribe(i -> count.incrementAndGet());

            // Feed the observable with 100 million messages
            IntStream.range(1, 100_000_000).forEach(observable::onNext);

            // Count how many have actually been received.
            System.out.println(count.get());
            System.out.println("==================");
        }
    }
}
