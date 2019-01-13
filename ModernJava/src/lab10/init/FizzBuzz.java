package lab10.init;

import java.util.Collections;
import java.util.List;

public class FizzBuzz
{
	
	private static List<String> getFizzBuzzList(int start, int end) {

		// Implement this
		return Collections.emptyList(); 
	}

	private static List<String> getFizzBuzzListInParallel(int start, int end) {

		// Implement this
		return Collections.emptyList();
	}

    public static void main(String... args)
    {
    	getFizzBuzzList(1, 100).forEach(System.out::println);
    	getFizzBuzzListInParallel(1, 100).forEach(System.out::println);


    	System.out.println("Size: " + getFizzBuzzListInParallel(1, 10_000_000).size());
    }
}
