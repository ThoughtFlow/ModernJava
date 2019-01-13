package chap06;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ExecutionServiceTest {
    public static void main(String... args) {
        {
            ExecutorService service = Executors.newCachedThreadPool();
            service.submit(() -> {System.out.println("Running in a thread: " + Thread.currentThread().getName());});

            service.shutdown();
        }

        {
            ExecutorService service = Executors.newCachedThreadPool();
            Future<Integer> future = service.submit(() -> 1);

            try {
                Integer result = future.get();
                System.out.println("Getting result: " + result);
            } catch (InterruptedException | ExecutionException e) {
                // Deal with error...
            }

            service.shutdown();
        }

        {
            ExecutorService service = Executors.newCachedThreadPool();
            Future<Integer> future = service.submit(() -> 1);

            try {
                Integer result = future.get(10, TimeUnit.SECONDS);
                System.out.println("Getting result: " + result);
            } catch (InterruptedException | TimeoutException | ExecutionException e) {
                // Deal with error...
            }

            service.shutdown();
        }
    }
}
