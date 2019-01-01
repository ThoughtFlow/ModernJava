package lab04.fin;

import java.util.function.Function;

public class CurriedFun {

	private static void concatStrings() {
		Function<String, Function<String, Function<String, String>>> func = s -> t -> u -> s + t + u;
		
		System.out.println(func.apply("Currying").apply(" is").apply(" great!"));
	}
	
	private static void multiply() {
		Function<Integer, Function<Integer, Function<Integer, Integer>>> func = x -> y -> z -> x * y * z;
		
		System.out.println(func.apply(5).apply(4).apply(3));
	}
	
	public static void main(String... args) {
		multiply();
		concatStrings();
	}
}
