package chap06;

public class ThreadedClassWithSynchronized {
    
    private int durationInSeconds1 = 0;
    private int durationInSeconds2 = 0;

    private int getTotalDuration() {
        int sum;
        synchronized(this) {
            sum = durationInSeconds1 + durationInSeconds2; // Thread-safe block
        }

        return sum;
    }

    private synchronized void incrementDuration1(int secondsA, int secondsB) {
        durationInSeconds1 += secondsA; // Thread-safe method
        durationInSeconds2 += secondsB;
    }
}

