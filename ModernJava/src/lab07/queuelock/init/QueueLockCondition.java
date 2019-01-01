 package lab07.queuelock.init;


public class QueueLockCondition implements SynchronizedQueue {

	private Element first, last;
	private int curSize, maxSize;

	public QueueLockCondition(int maxSize) {
		this.maxSize = maxSize;
	}

	@Override
	public synchronized void put(Object o) throws InterruptedException {
		// Refactor this method
		while (this.curSize == this.maxSize) {
			this.wait();
		}
		
		if (this.first == null) {
			this.first = (this.last = new Element(o));
		} 
		else {
			this.last = (this.last.next = new Element(o));
		}
		
		this.curSize++;
		this.notifyAll();
	}

	@Override
	public synchronized Object get() throws InterruptedException {
		// Refactor this method
		while (this.curSize == 0) {
			this.wait();
		}
		
		Object o = this.first.value;
		this.first = this.first.next;
		this.curSize--;
		this.notifyAll();
		
		return o;
	}
	
	private static class Element {
		final Object value;
		Element next;


		Element(Object value) {
			this.value = value;
		}
	}
}
