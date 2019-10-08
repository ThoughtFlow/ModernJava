package lab05.init;

@SuppressWarnings("SameParameterValue")
public class StreamFun {

	private static void printOnlyEventNumbers(int range) {
		// Print only even numbers
		System.out.println("Printing even numbers from 0 to " + range + "...");

		// ### Implement stream ###
		System.out.println("Printing even numbers from 0 to " + range + "...Done");
	}

	private static void printSumOfOddNumbers(int range) {
		// Print the sum of odd numbers from 0 to 100
		System.out.print("The sum of odd numbers from 0 to " + range + " is: ");

		// ### Implement stream ###
		System.out.println("???");
	}

	private static void printSmallestIntForFactorial(int factorial) {
		// Find the smallest int whose factorial is >= factorial
		// ### Implement stream ###
		int smallestInt = 0;

		System.out.println("Smallest int whose factorial is >= " + factorial + " is: " + smallestInt);
	}

	public static void main(String... args) {
		printOnlyEventNumbers(100);
		printSumOfOddNumbers(100);
		printSmallestIntForFactorial(1_000_000);
	}
}
