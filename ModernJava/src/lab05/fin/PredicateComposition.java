package lab05.fin;

import java.util.Arrays;
import java.util.function.Predicate;

public class PredicateComposition
{
	
	private static void runTest(Predicate<Double[]> hasPassed) {
		
        // Test your lambdas
        // True: Passed all
        Double[] scores = new Double[] {.65, .90, .90, .90, .90, .90};
        System.out.println("Should be true: " + hasPassed.test(scores));

        // False: Not all passed
        scores = new Double[] {.59, .90, .90, .90, .90, .9};
        System.out.println("Should be false: " + hasPassed.test(scores));

        // False: C average - fail
        scores = new Double[] {.70, .70, .70, .70, .70, .70};
        System.out.println("Should be false: " + hasPassed.test(scores));

        // True: C average but aced last
        scores = new Double[] {.70, .70, .70, .70, .70, 1d};
        System.out.println("Should be true: " + hasPassed.test(scores));

        // True: Failed first by scored perfect on last
        scores = new Double[] {.59, .90, .90, .90, .90, 1d};
        System.out.println("Should be true: " + hasPassed.test(scores));
        
        // False: same as previous but missed a test
        scores = new Double[] {.59, .90, .90, .90, 0d, 1d};
        System.out.println("Should be false: " + hasPassed.test(scores));

        // False: Perfect but missed last - fail!
        scores = new Double[] {1d, 1d, 1d, 1d, 1d, 0d};
        System.out.println("Should be false: " + hasPassed.test(scores));
	}
	
    public static void main(String[] args)
    {
        Predicate<Double[]> isAllPassed = scores -> Arrays.stream(scores).allMatch(next -> next >= .60d);

        Predicate<Double[]> isBAverage = scores -> (Arrays.stream(scores).reduce(0d, Double::sum) / scores.length) > .8;

        Predicate<Double[]> isLastPerfect = scores -> scores[scores.length - 1] == 1;
        
        Predicate<Double[]> isAnyMissed = scores -> Arrays.stream(scores).anyMatch(next -> next == 0);

    	// Compose the lambdas into one
        Predicate<Double[]> hasPassed = isAllPassed.and(isBAverage).or(isLastPerfect).and(isAnyMissed.negate());
        runTest(hasPassed);
    }
}
