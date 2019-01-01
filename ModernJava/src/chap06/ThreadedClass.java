package chap06;

public class ThreadedClass {
    private int durationInSeconds1 = 0;
    private int durationInSeconds2 = 0;

    private int getTotalDuration() {
        return durationInSeconds1 + durationInSeconds2;
    }

    private void incrementDuration(int secondsA, int secondsB) {
        durationInSeconds1 += secondsA; // Not thread-safe
        durationInSeconds2 += secondsB; // Not thread-safe
    }
}

