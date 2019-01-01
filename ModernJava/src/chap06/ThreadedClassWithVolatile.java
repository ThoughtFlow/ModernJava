package chap06;

public class ThreadedClassWithVolatile {
    private volatile int durationInSeconds1 = 0;
    private int durationInSeconds2 = 0;

    private int getTotalDuration() {
        return durationInSeconds1 + durationInSeconds2;
    }

    private void incrementDuration(int secondsA, int secondsB) {
        durationInSeconds1 += secondsA; // Thread safe but brittle
        durationInSeconds2 += secondsB; // Thread safe but brittle
    }
}

