package chap06;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class QueueWithCondition {
    private static class Element {
        final Object value;
        Element next;

        Element(Object value) {
            this.value = value;
        }
    }

    private Element first, last;
    private int curSize, maxSize;
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Condition condition = lock.writeLock().newCondition(); // This is created once only.


    public QueueWithCondition(int maxSize) {
        this.maxSize = maxSize;
    }

    public void put(Object o) throws InterruptedException {
        try {
            lock.writeLock().lock(); // Must use write locks
            while (this.curSize == this.maxSize) {
                condition.await();
            }
            if (this.first == null) {
                this.first = (this.last = new Element(o));
            } else {
                this.last = (this.last.next = new Element(o));
            }
            this.curSize++;
            condition.signalAll();
        } finally {
            lock.writeLock().unlock(); // Make sure the unlock is in a finally block.
        }
    }

        public Object get() throws InterruptedException {
            try {
                lock.writeLock().lock();
                while (this.curSize == 0) {
                    condition.await();
                }
                Object o = this.first.value;
                this.first = this.first.next;
                this.curSize--;
                condition.signalAll();
                return o;
            } finally {
                lock.writeLock().unlock(); // Make sure the unlock is in a finally block.
            }
        }


        public static void main(String... args) throws InterruptedException {
            QueueWithCondition q = new QueueWithCondition(2);
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

