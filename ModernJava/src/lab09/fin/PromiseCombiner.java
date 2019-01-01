package lab09.fin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;

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
		CompletableFuture<String> promise = CompletableFuture.supplyAsync(PromiseCombiner::produceString1);
		CompletableFuture<String> promise2 = CompletableFuture.supplyAsync(PromiseCombiner::produceString2);
		CompletableFuture<String> promise3 = CompletableFuture.supplyAsync(PromiseCombiner::produceString3);
		
		BiFunction<String, String, String> combiner = (s1, s2) -> s1.length() > s2.length() ? s1 : s2;
		CompletableFuture<String> promise4 = promise.thenCombine(promise2, combiner).thenCombine(promise3, combiner);
		CompletableFuture<Integer> promise5 = promise4.thenApply(String::length);
		
		return promise5;
	}
	
	public static void main(String... args) throws InterruptedException, ExecutionException {
		System.out.println(weavePromises().get());
	}
}
