package lab01.fin;

public class LambdaTest {

	private static void calculateAndConsume(int operand, Interface2 calculator, Interface4 consumer) {
		consumer.consume(calculator.getSquareOfA(operand));
	}

	public static void main(String... args) {
        {
            // Implement a lambda squares the parameter value and prints the result conforming to Interface1.
            Interface1 i1 = x -> System.out.println(x * x);
            i1.printSquareOfA(3);
        }

        {
            // Implement a lambda that will return the square of the parameter value using interface2.
            Interface2 i2 = x -> x * x;
            System.out.println(i2.getSquareOfA(3));
        }

        {
            // Implement a lambda that will return the multiplication of the two parameter values using Interface3.
            Interface3 i3 = (x, y) -> x * y;
            System.out.println(i3.getAxB(3, 3));
        }

        {
            // Implement a lambda that will return the value of pi as a double using Interface5.
            Interface5 i5 = () -> Math.PI;
            System.out.println(i5.getPi());
        }

        {
            // Implement a lambda that returns the square of the parameter value conforming to Interface2.
            // Provide a default method that pretty prints a message around getSquareOfA().
            Interface2 i2 = x -> x * x;
            System.out.println(i2.stringedSquareOfA(3));
        }

        {
            // Implement a static method in Interface3 that multiplies the two numbers.
            // Call the method and print the result.
            System.out.println(Interface3.defaultGetAxB(3, 3));
        }

        {
            // Implement a static method reference for Interface4 and use calculateAndConsume with Interface2
            // to square the value of the parameter and print the results.
            Interface2 i2 = x -> x * x;
            Interface4 i4 = System.out::println;
            calculateAndConsume(3, i2, i4);
        }

        {
            // Implement a lambda that uses the static method Double.valueOf() to implement interface6
            Interface6 i6 = Double::valueOf;
            System.out.println(i6.create(3));
        }
	}
}
