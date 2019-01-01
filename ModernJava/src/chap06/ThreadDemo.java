package chap06;

public class ThreadDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new InterfaceRunner("T1"));
        Thread t2 = new ExtendsRunner("T2");
        t1.start(); t2.start();
        t1.join(); t2.join();
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
    private static class ExtendsRunner extends Thread {
        private final String threadName;

        public ExtendsRunner(String threadName) {
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
