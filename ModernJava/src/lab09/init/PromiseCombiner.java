package lab09.init;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("unused")
public class PromiseCombiner {

	private static String produceString1() {
		return "Scala";
	}
	
	private static String produceString2() {
		return "JavaScript";
	}
	
	private static String produceString3() {
		return "Kotlin";
	}
	
	private static CompletableFuture<Integer> weavePromises() {

		// Implement this
		return CompletableFuture.completedFuture(0);
	}
	
	public static void main(String... args) throws InterruptedException, ExecutionException {
		System.out.println(weavePromises().get());
	}
}
