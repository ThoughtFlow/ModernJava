package chap06;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadedClassWithAtomics {
    private AtomicInteger durationInSeconds1 = new AtomicInteger(0);
    private AtomicInteger durationInSeconds2 = new AtomicInteger(0);

    private int getTotalDuration() {
        return durationInSeconds1.get() + durationInSeconds2.get(); // thread-safe get
    }

    private void incrementDuration(int secondsA, int secondsB) {
        durationInSeconds1.addAndGet(secondsA); // Thread-safe add
        durationInSeconds2.addAndGet(secondsB);
    }
}

