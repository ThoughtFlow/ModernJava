package chap07;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * QuickSort algorithm implemented using the Fork/Join framework. List is updated in-place.
 */
public class QuickSortAction extends RecursiveAction {

    private final List<Integer> toSort;
    private final int startingIndex;
    private final int size;

    private QuickSortAction(List<Integer> toSort, int startingIndex, int size)
    {
        this.toSort = toSort;
        this.startingIndex = startingIndex;
        this.size = size;
    }

    @Override
    protected void compute() {

        int low = startingIndex;
        int high = size;

        if (low < size) {

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

            QuickSortAction leftHalf = new QuickSortAction(toSort, startingIndex, low);
            leftHalf.fork();
            QuickSortAction rightHalf = new QuickSortAction(toSort, low == startingIndex ? low + 1 : low, size);
            rightHalf.fork();
            leftHalf.join();
            rightHalf.join();
        }
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

        System.out.println("Random numbers generated: ");
        for (int number : toSort) {
            System.out.println(" - " + number);
        }

        QuickSortAction sort = new QuickSortAction(toSort, 0, toSort.size() - 1);
		ForkJoinPool executor = new ForkJoinPool();
        ForkJoinTask<Void> action = executor.submit(sort);

        action.get();

        System.out.println("Random numbers sorted: ");
        for (int number : toSort) {
            System.out.println(" - " + number);
        }

        executor.shutdown();
    }
}
