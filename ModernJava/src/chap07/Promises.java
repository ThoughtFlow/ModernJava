package chap07;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Promises {

    private static int doTask(boolean isSuccessSimulation) {
        if (!isSuccessSimulation) {
            throw new RuntimeException("Simulated Exception");
        }

        // Always return 2;
        return 2;
    }

    private static void printTestHeader(String message) {
        System.out.println("======================");
        System.out.println(message);
        System.out.println("======================");
    }

    private static void promiseOne() throws InterruptedException, ExecutionException {
        printTestHeader("All tasks run under same thread sequentially using the common fork-join thread pool");
        CompletableFuture<Integer> task1 = CompletableFuture.supplyAsync(() -> doTask(true));
        CompletableFuture<Integer> task2 = task1.thenApply(i -> i * 2);
        CompletableFuture<Integer> task3 = task2.thenApply(i -> (int) Math.pow(i, 2));
        CompletableFuture<Integer> task4 = task3.thenApply(i -> (int) Math.pow(i, 3));

        System.out.println("Result: " + task1.get());
        System.out.println("Result: " + task2.get());
        System.out.println("Result: " + task3.get());
        System.out.println("Result: " + task4.get());
    }

    private static void promiseTwo() throws InterruptedException, ExecutionException {
        printTestHeader("Task 1 & 3 run in parallel on multiple threads, 2 and 4 run sequentially on any previous task thread");
        CompletableFuture<Integer> task1a = CompletableFuture.supplyAsync(() -> doTask(true));
        CompletableFuture<Integer> task1b = CompletableFuture.supplyAsync(() -> doTask(true));
        CompletableFuture<Integer> task1c = CompletableFuture.supplyAsync(() -> doTask(true));

        CompletableFuture<Integer> task2 =
                task1a.thenCombine(task1b, (ia, ib) -> ia + ib).
                        thenCombine(task1c, (iab, ic) -> iab + ic).thenApply(i -> i * 2);

        CompletableFuture<Integer> task3a = task2.thenApplyAsync(i -> (int) Math.pow(i, 2));
        CompletableFuture<Integer> task3b = task2.thenApplyAsync(i -> (int) Math.pow(i, 2));
        CompletableFuture<Integer> task3c = task2.thenApplyAsync(i -> (int) Math.pow(i, 2));

        CompletableFuture<Integer> task4 =
                task3a.thenCombine(task3b, (ia, ib) -> ia + ib).
                        thenCombine(task3c, (iab, ic) -> iab + ic).
                        thenApply(i -> (int) Math.pow(i, 3));

        System.out.println("Result: " + task4.get());
    }

    private static void promiseThree() throws InterruptedException, ExecutionException {
        printTestHeader("Task 1 & 3 run in parallel on multiple threads, 2 and 4 run sequentially on any previous task thread - " +
                "with exception handling");
        CompletableFuture<Integer> task1a = CompletableFuture.supplyAsync(() -> doTask(false)).exceptionally(e -> 1);
        CompletableFuture<Integer> task1b = CompletableFuture.supplyAsync(() -> doTask(true)).exceptionally(e -> 1);
        CompletableFuture<Integer> task1c = CompletableFuture.supplyAsync(() -> doTask(true)).exceptionally(e -> 1);

        CompletableFuture<Integer> task2 =
                task1a.thenCombine(task1b, (ia, ib) -> ia + ib).
                        thenCombine(task1c, (iab, ic) -> iab + ic).
                        thenApply(i -> i * 2);

        CompletableFuture<Integer> task3a = task2.thenApplyAsync(i -> (int) Math.pow(i, 2));
        CompletableFuture<Integer> task3b = task2.thenApplyAsync(i -> (int) Math.pow(i, 2));
        CompletableFuture<Integer> task3c = task2.thenApplyAsync(i -> (int) Math.pow(i, 2));

        CompletableFuture<Integer> task4 =
                task3a.thenCombine(task3b, (ia, ib) -> ia + ib).
                        thenCombine(task3c, (iab, ic) -> iab + ic).
                        thenApply(i -> (int) Math.pow(i, 3));

        System.out.println("Result: " + task4.get());
    }

    private static void promiseFour() throws InterruptedException, ExecutionException {
        printTestHeader("All tasks run under same thread sequentially using this thread");
        CompletableFuture<Integer> task1 = new CompletableFuture<>();
        CompletableFuture<Integer> task2 = task1.thenApply(i -> i * 2);
        CompletableFuture<Integer> task3 = task2.thenApply(i -> (int) Math.pow(i, 2));
        CompletableFuture<Integer> task4 = task3.thenApply(i -> (int) Math.pow(i, 3));

        task1.complete(doTask(true));
        System.out.println("Result: " + task4.get());

    }


    private static void promiseFive() throws InterruptedException, ExecutionException {
        printTestHeader("Task 1 and 2 run on the main thread sequentially, 3 runs in parallel on multiple threads, " +
                        "and 4 runs sequentially on any previous task thread - with manual exception handling");
        CompletableFuture<Integer> task1a = new CompletableFuture<>();
        CompletableFuture<Integer> task1b = new CompletableFuture<>();
        CompletableFuture<Integer> task1c = new CompletableFuture<>();

        task1a.completeExceptionally(new IllegalArgumentException("Synthesized error"));
        task1b.completeExceptionally(new IllegalArgumentException("Synthesized error"));
        task1c.completeExceptionally(new IllegalArgumentException("Synthesized error"));

        // Must be added AFTER the task has executed
        task1a = task1a.exceptionally(e -> 1);
        task1b = task1b.exceptionally(e -> 1);
        task1c = task1c.exceptionally(e -> 1);

        CompletableFuture<Integer> task2 =
                task1a.thenCombine(task1b, (ia, ib) -> ia + ib).
                        thenCombine(task1c, (iab, ic) -> iab + ic).
                        thenApply(i -> i * 2);

        CompletableFuture<Integer> task3a = task2.thenApplyAsync(i -> (int) Math.pow(i, 2));
        CompletableFuture<Integer> task3b = task2.thenApplyAsync(i -> (int) Math.pow(i, 2));
        CompletableFuture<Integer> task3c = task2.thenApplyAsync(i -> (int) Math.pow(i, 2));

        CompletableFuture<Integer> task4 =
                task3a.thenCombine(task3b, (ia, ib) -> ia + ib).
                        thenCombine(task3c, (iab, ic) -> iab + ic).
                        thenApply(i -> (int) Math.pow(i, 3));

        System.out.println("Result: " + task4.get());
    }

    private static void promiseSix() throws InterruptedException, ExecutionException {
        printTestHeader("Task 1 and 2 run on the main thread sequentially, 3 runs in parallel on multiple threads, " +
                         "and 4 runs sequentially on any previous task thread - with whenComplete");
        CompletableFuture<Integer> task1a = new CompletableFuture<>();
        CompletableFuture<Integer> task1b = new CompletableFuture<>();
        CompletableFuture<Integer> task1c = new CompletableFuture<>();

        task1a.completeExceptionally(new IllegalArgumentException("Synthesized error"));
        task1b.completeExceptionally(new IllegalArgumentException("Synthesized error"));
        task1c.completeExceptionally(new IllegalArgumentException("Synthesized error"));

        CompletableFuture<Integer> task2 =
                task1a.thenCombine(task1b, (ia, ib) -> ia + ib).
                        thenCombine(task1c, (iab, ic) -> iab + ic).
                        thenApply(i -> i * 2);

        CompletableFuture<Integer> task3a = task2.thenApplyAsync(i -> (int) Math.pow(i, 2));
        CompletableFuture<Integer> task3b = task2.thenApplyAsync(i -> (int) Math.pow(i, 2));
        CompletableFuture<Integer> task3c = task2.thenApplyAsync(i -> (int) Math.pow(i, 2));

        CompletableFuture<Integer> task4 =
                task3a.thenCombine(task3b, (ia, ib) -> ia + ib).
                        thenCombine(task3c, (iab, ic) -> iab + ic).thenApply(i -> (int) Math.pow(i, 3)).
                        whenComplete((i, e) -> {
                            if (i != null) {
                                System.out.println(i);
                            }
                            else {
                                System.err.println("An error occured");
                            }
                        });

        System.out.println("Result: " + task4.get());
    }

    private static void xxx() {
        CompletableFuture<Integer> task1a = new CompletableFuture<>();
        task1a.thenCombine()
    }



    public static void main(String... args) throws InterruptedException, ExecutionException {
        promiseOne();
        promiseTwo();
        promiseThree();
        promiseFour();
        promiseFive();

        // This will throw an error
        //promiseSix();
    }
}
