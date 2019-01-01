package lab04.init;

import java.util.function.Function;

public class CurriedFun {

	@SuppressWarnings("null")
	private static void concatStrings() {
		// Implement this function.
		Function<String, Function<String, Function<String, String>>> func = null;
		
		System.out.println(func.apply("Currying").apply(" is").apply(" great!"));
	}
	
	private static void multiply() {

		// Define func as a function that takes an integer that return a function that takes an integer that return a function that takes and
		// returns an integer.
//		System.out.println(func.apply(5).apply(4).apply(3));
	}
	
	public static void main(String... args) {
		multiply();
		concatStrings();
	}
}