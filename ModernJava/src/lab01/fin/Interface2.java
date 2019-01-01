package lab01.fin;

@FunctionalInterface
public interface Interface2 {

	public int getSquareOfA(int a);
	
	default String stringedSquareOfA(int a) {
		return "The square of " + a + " is " + getSquareOfA(a);
	}
	
}
