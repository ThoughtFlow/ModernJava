package lab04.init;

import java.util.Arrays;

import java.util.List;
import java.util.function.Function;

@SuppressWarnings("unused")
public class CurriedGrading
{
	// Implement the currying function
	private static Function<GradeCalcType, Function<List<Double>, Double>> curryingFunction;

	public static void main(String... args)
    {
    	List<Double> scores = Arrays.asList(.65, .75, .85);
    	
    	// Implement the partial application to the currying function for AVERAGE, BEST and WORST.
    }

    private enum GradeCalcType
    {
        AVERAGE,
        WORST,
        BEST
    }
}