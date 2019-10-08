package lab05.fin;

import java.util.stream.IntStream;

@SuppressWarnings("SameParameterValue")
public class StreamFun {

	private static void printOnlyEventNumbers(int range) {
		// Print only even numbers
		System.out.println("Printing even numbers from 0 to " + range + "...");
		IntStream.rangeClosed(0, 100).filter(i -> i % 2 == 0).forEach(System.out::println);
		System.out.println("Printing even numbers from 0 to " + range + "...Done");
	}

	private static void printSumOfOddNumbers(int range) {
		// Print the sum of odd numbers from 0 to 100
		System.out.print("The sum of odd numbers from 0 to " + range + " is: ");
		System.out.println(IntStream.rangeClosed(0, range).filter(i -> i % 2 == 1).sum());
            //	.reduce(0, (l, r) -> l + r));
			// Can also use sum instead of reduce on IntStream
			// System.out.println("The sum of odd numbers from 0 to 100 is: "
			//		+ IntStream.rangeClosed(0, 100).filter(i -> i % 2 == 1).sum());
	}

	private static void printSmallestIntForFactorial(int factorial) {
		// Find the smallest int whose factorial is >= factorial
		int smallestInt = IntStream.rangeClosed(1, 100).
				filter(i -> IntStream.rangeClosed(1, i).reduce(1, (l, r) -> l * r) >= 1000000).
				findAny().
				orElse(0);
		System.out.println("Smallest int whose factorial is >= " + factorial + " is: " + smallestInt);
	}

	public static void main(String... args) {
		printOnlyEventNumbers(100);
		printSumOfOddNumbers(100);
		printSmallestIntForFactorial(1_000_000);
	}
}
