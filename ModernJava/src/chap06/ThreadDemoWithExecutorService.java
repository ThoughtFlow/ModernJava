package chap06;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadDemoWithExecutorService {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(2);
        Runnable t1 = new InterfaceRunner("T1");
        Runnable t2 = new InterfaceRunner("T2");
        service.submit(t1);
        service.submit(t2);
        service.awaitTermination(5, TimeUnit.SECONDS);
        service.shutdown();
    }

    private static class InterfaceRunner implements Runnable {
        private final String threadName;

        public InterfaceRunner(String threadName) {
            this.threadName = threadName;
        }
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                System.out.println(threadName + "=" + i);
            }
        }
    }
}
