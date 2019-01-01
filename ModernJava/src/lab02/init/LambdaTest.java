package lab02.init;

public class LambdaTest {

	public static void main(String... args) {

		// As a lambda block
		Interface1 i1 = x -> {System.out.println(x * x);};
		i1.printSquareOfA(3);
		
		Interface2 i2 = x -> x * x;
		System.out.println(i2.getSquareOfA(3));

		Interface3 i3 = (x, y) -> x * y;
		System.out.println(i3.getAxB(3,  3));

		Interface4 i4 = () -> Math.PI;
		System.out.println(i4.getPi());
		
		Interface5 i5 = x -> x == 10;
		System.out.println(i5.isEqualToTen(10));
	}
}
