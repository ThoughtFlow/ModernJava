package chap06;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BlockingMap<K, V> {
    private Map<K, V> map = new HashMap<>();
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Condition condition = lock.writeLock().newCondition();

    public V get(K key) {
        lock.readLock().lock();
        try {
            return map.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    public V require(K key) {
        V value = get(key); // try cheap first
        if (value == null) {
            lock.writeLock().lock();
            try {
                while ((value = map.get(key)) == null) {
                    condition.await();
                }
            } catch (InterruptedException giveup) {

            } finally {
                lock.writeLock().unlock();
            }
        }
        return value;
    }

    public void set(K key, V value) {
        lock.writeLock().lock();
        try {
            map.put(key, value);
            condition.signalAll();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static void main(String... args) throws InterruptedException {
        BlockingMap<String, String> map = new BlockingMap<>();

        Thread producer = new Thread(() -> {
            try {
                long startTime = System.currentTimeMillis();
                for (int index = 0; index < 10; ++index) {
                    long endTime = System.currentTimeMillis() - startTime;
                    map.set(Integer.toString(index), "Message: " + endTime);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        });

        Thread consumer = new Thread(() -> {
            for (int index = 0; index < 10; ++index) {
                String message = map.require(Integer.toString(index));
                System.out.println(message);
            }
        });

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
    }
}