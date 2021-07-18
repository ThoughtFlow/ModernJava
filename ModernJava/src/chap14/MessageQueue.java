package chap14;

import java.util.LinkedList;
import java.util.Queue;

public class MessageQueue {

    // a queue of buffers that have been read and are ready to be written
    private final Queue<String> stringQueue = new LinkedList<>();

    public void enqueue(String string) {
        this.stringQueue.add(string);
    }

    public String peek() {
        return this.stringQueue.peek();
    }

    public String dequeue() {
        return this.stringQueue.remove();
    }

    public boolean isQueueEmpty() {
        return this.stringQueue.isEmpty();
    }
}