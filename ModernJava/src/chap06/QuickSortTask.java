package chap06;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class QuickSortTask extends RecursiveTask<List<String>> {

    private final List<Integer> toSort;
    private final List<String> threads;
    private final int startingIndex;
    private final int size;

    private QuickSortTask(List<Integer> toSort, int startingIndex, int size, List<String> threads)
    {
        this.toSort = toSort;
        this.startingIndex = startingIndex;
        this.size = size;
        this.threads = threads;
    }

    @Override
    protected List<String> compute() {

        int low = startingIndex;
        int high = size;

        if (low < size) {

            threads.add(Thread.currentThread().getName() + ": " + startingIndex + " " + size);

            int mid = toSort.get((low + high) / 2);
            while (low < high) {
                while (low < high && toSort.get(low) < mid) {
                    low++;
                }
                while (low < high && toSort.get(high) > mid) {
                    high--;
                }
                if (low < high) {
                    // Swap values
                    int temp = toSort.get(low);
                    toSort.set(low, toSort.get(high));
                    toSort.set(high, temp);
                    low++;
                    high--;
                }
            }

            if (high < low) {
                low = high;
            }

            QuickSortTask leftHalf = new QuickSortTask(toSort, startingIndex, low, threads);
            leftHalf.fork();
            QuickSortTask rightHalf = new QuickSortTask(toSort, low == startingIndex ? low + 1 : low, size, threads);
            rightHalf.fork();
            leftHalf.join();
            rightHalf.join();
        }

        return threads;
    }


    private static List<Integer> generateRandomList(int size) {

        ArrayList<Integer> randomInts = new ArrayList<>(size);
        Random randomNumberGenerator = new Random();
        for (int index = 0; index < size; ++index) {
            randomInts.add(randomNumberGenerator.nextInt(size));
        }

        return randomInts;
    }

    public static void main(String... args) throws InterruptedException, ExecutionException {
        List<Integer> toSort = generateRandomList(1000);

        QuickSortTask sort = new QuickSortTask(toSort, 0, toSort.size() - 1, new ArrayList<>());
		ForkJoinPool executor = (ForkJoinPool) Executors.newWorkStealingPool();
        ForkJoinTask<List<String>> task = executor.submit(sort);

        for (String nextThread : task.get()) {
            System.out.println(nextThread);
        }

        for (int number : toSort) {
            System.out.println(number);
        }

        executor.shutdown();
    }


}
