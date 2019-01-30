package chap06;

import lab.util.Util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static lab.util.Util.logWithThread;
import static lab.util.Util.sleep;

/**
 * Class to demo the Thread Scheduler.
 */
public class ThreadSchedulerDemo {

    private static void demoScheduler(ScheduledExecutorService service)
            throws InterruptedException, TimeoutException, ExecutionException  {

        {
            // Runs once - after a delay
            logWithThread("Running scheduler ===================================");
            ScheduledFuture<?> result = service.schedule(() -> logWithThread("Run after 3 seconds"), 3, TimeUnit.SECONDS);
            result.get(5, TimeUnit.SECONDS);
            logWithThread("=====================================================");
        }

        {
            // Runs once - after a delay - and returns a value
            logWithThread("Running scheduler with return value =================");
            ScheduledFuture<String> result = service.schedule(() -> "Return a result after 3 seconds", 3, TimeUnit.SECONDS);
            logWithThread(result.get(10, TimeUnit.SECONDS));
            logWithThread("=====================================================");
        }

        {
            // Runs as long as the get() is willing to wait - starts every cycle after 3 seconds unless previous cycle
            // is not yet complete
            logWithThread("Running scheduler at fixed rate =====================");
            ScheduledFuture<?> result = service.scheduleAtFixedRate(() -> {
                logWithThread("Run every 3 seconds after 3 seconds - fixed rate");
                sleep(2000);
            }, 3, 3, TimeUnit.SECONDS);

            try {
                result.get(20, TimeUnit.SECONDS);
            }
            catch (TimeoutException e) {
                result.cancel(true);
            }
            logWithThread("=====================================================");
        }

        {
            // Runs as long as the get() is willing to wait - starts every cycle after 3 seconds from previous cycle
            logWithThread("Running scheduler at fixed delay ====================");
            ScheduledFuture<?> result = service.scheduleWithFixedDelay(() -> {
                logWithThread("Run every 3 seconds after 3 seconds - fixed delay");
                sleep(1000);
            }, 3, 3, TimeUnit.SECONDS);

            try {
                result.get(10, TimeUnit.SECONDS);
            }
            catch (TimeoutException e) {
                result.cancel(true);
            }
            logWithThread("=====================================================");
        }
    }

    public static void main(String... args) throws Exception {

        ScheduledExecutorService service = Executors.newScheduledThreadPool(2);

        demoScheduler(service);

        service.shutdown();
    }
}