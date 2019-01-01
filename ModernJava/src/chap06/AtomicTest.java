package chap06;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple test of AtomicInteger with multiple threads.
 */
public class AtomicTest {

    public static void main(String... args) throws InterruptedException {
        AtomicInteger count = new AtomicInteger(0);
        Runnable runnable = () -> {
            for (int index = 0; index < 1_000_000; ++index) {
                count.incrementAndGet();
            }
            ;
        };

        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Final value: " + count.get());
    }
}
