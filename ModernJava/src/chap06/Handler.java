package chap06;

public class Handler implements Runnable {
    private Exception e = null;
    private Long result = null;

    public synchronized void run() {
        long t = System.currentTimeMillis();
        try {
            // handle request
            this.result = System.currentTimeMillis() - t;
        } catch (Exception e) {
            this.e = e;
        } finally {
            this.notifyAll();
        }
    }

    public synchronized long getResult() throws Exception {
        while (result == null && e == null) {
            this.wait();
        }
        if (e != null) {
            throw e;
        } else {
            return result;
        }
    }

    public static void main(String... args) throws Exception {
        Handler handler = new Handler();
        Thread t = new Thread(handler);
        t.start();
        t.join();
        System.out.println(handler.getResult());
    }
}

