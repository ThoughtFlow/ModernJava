package lab01.init;

public class LambdaTest {

	private static void calculateAndConsume(int operand, Interface2 calculator, Interface4 consumer) {
		consumer.consume(calculator.getSquareOfA(operand));
	}

	public static void main(String... args) {

		{
			// Implement a lambda squares the parameter value and prints the result conforming to Interface1.
		}

		{
			// Implement a lambda that will return the square of the parameter value using interface2.
		}

		{
			// Implement a lambda that will return the multiplication of the two parameter values using Interface3.
		}

		{
			// Implement a lambda that will return the value of pi as a double using Interface5.
		}

		{
			// Implement a lambda that returns the square of the parameter value conforming to Interface2.
			// Provide a default method that pretty prints a message around getSquareOfA().
		}

		{
			// Implement a static method in Interface3 that multiplies the two numbers.
			// Call the method and print the result.
		}

		{
			// Implement a static method reference for Interface4 and use calculateAndConsume with Interface2
			// to square the value of the parameter and print the results.
		}

		{
			// Implement a lambda that uses the static method Double.valueOf() to implement interface6
		}
	}
}
