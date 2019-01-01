package chap06;

public class Queue {
    private static class Element {
        final Object value;
        Element next;

        Element(Object value) {
            this.value = value;
        }
    }

    private Element first, last;
    private int curSize, maxSize;

    public Queue(int maxSize) {
        this.maxSize = maxSize;
    }

    public synchronized void put(Object o) throws InterruptedException {
        while (this.curSize == this.maxSize) {
            this.wait();
        }
        if (this.first == null) {
            this.first = (this.last = new Element(o));
        } else {
            this.last = (this.last.next = new Element(o));
        }
        this.curSize++;
        this.notifyAll();
    }

    public synchronized Object get() throws InterruptedException {
        while (this.curSize == 0) {
            this.wait();
        }
        Object o = this.first.value;
        this.first = this.first.next;
        this.curSize--;
        this.notifyAll();
        return o;
    }

    public static void main(String... args) throws InterruptedException {
        Queue q = new Queue(2);
        Thread producer = new Thread(() -> {
            try {
                long startTime = System.currentTimeMillis();
                for (int index = 0; index < 10; ++index) {
                    long endTime = System.currentTimeMillis() - startTime;
                    q.put("Message: " + index + ": " + endTime);
                }
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int index = 0; index < 10; ++index) {
                    Thread.sleep(1000);
                    System.out.println(q.get());
                }
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        });

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
    }
}

