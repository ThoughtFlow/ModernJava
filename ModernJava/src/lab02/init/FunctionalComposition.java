package lab02.init;

import java.util.function.Function;

public class FunctionalComposition
{
    public static void main(String... args)
    {
    	// todo Define the 4 functions here

        // todo Compose using andThen()
    	Function<Integer, Integer> full = x -> x; // implement this
        System.out.println(full.apply(3));

        // todo Compose using compose()
    	full = x -> x; // implement this
        System.out.println(full.apply(3));
    }
}
