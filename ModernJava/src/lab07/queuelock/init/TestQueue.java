package lab07.queuelock.init;

import java.util.HashMap;
import java.util.Map;

import lab.util.Util;

public class TestQueue {

	public static void testQueue(SynchronizedQueue queue) {
		Map<Object, Boolean> check = new HashMap<>();


		System.out.println("=============");
		System.out.println("Testing with: " + queue.getClass());
		Thread getterThread = new Thread(() -> {
			Object object = null;
			do {
				try {
					Util.doWork(1);
					if ((object = queue.get()) != null) {
						check.put(object, true);
						System.out.println(System.currentTimeMillis() + ": Getting: " + object);
					}
				} catch (InterruptedException exception) {
					System.err.println("Caught exception: " + exception);
				}
			} while (object != null);
		});

		getterThread.start();

		try {
			Util.doWork(5);
			for (int index = 0; index < 10; ++index) {
				System.out.println(System.currentTimeMillis() + ": Putting: " + index);
				check.put(index, false);
				queue.put(index);
			}
			queue.put(null);
			getterThread.join();
		} catch (InterruptedException exception) {
			System.err.println("Caught exception: " + exception);
		}
		
		// Check that all items were fetched.
		if (check.keySet().stream().anyMatch(k -> check.get(k) == false)) {
			check.keySet().forEach(key -> 
			{
				if (!check.get(key))
				{
					System.out.println("Error: Item not fetched: " + key);
				}
			});
		}
		else {
			System.out.println("All queue items fetched");
		}
	}

	public static void main(String... args) throws Exception {
		testQueue(new QueueNotifyWait(5));
		testQueue(new QueueLockCondition(5));
	}
}
