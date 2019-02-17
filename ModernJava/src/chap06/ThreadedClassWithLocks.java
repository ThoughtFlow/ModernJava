package chap06;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ThreadedClassWithLocks {
    private int durationInSeconds1 = 0;
    private int durationInSeconds2 = 0;
    private final ReadWriteLock locker = new ReentrantReadWriteLock();

    private int getTotalDuration() {
        int sum = 0;
        try {
            locker.readLock().lock();
            sum = durationInSeconds1 + durationInSeconds2; // Thread-safe block
        } finally {
        	// Always use finally block
            locker.readLock().unlock();
        }

        return sum;
    }

    private void incrementDuration(int secondsA, int secondsB) {
        try {
            locker.writeLock().lock();
            durationInSeconds1 += secondsA; // Thread-safe block
            durationInSeconds2 += secondsB;
        } finally {
        	// Always use finally block
            locker.writeLock().unlock();
        }
    }
}
