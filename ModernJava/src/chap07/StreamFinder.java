package chap07;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class StreamFinder {

	public static Map<String, List<String>> find(String searchString, Stream<String> stream) {
		final String lowerCaseSearchString = searchString.toLowerCase();
		
		return stream.
				parallel().
				map(s -> s.toLowerCase()).
				filter(s -> s.contains(lowerCaseSearchString)).
				collect(Collectors.groupingBy(f -> "Thread " + Long.toString(Thread.currentThread().getId())));
	}
	
	public static void main(String... args) throws Exception {
	
		// Try with an unknown size
		{
			URL url = new URL("https://www.oracle.com");
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
			List<String> list = reader.lines().collect(Collectors.toList());
			find("oracle", list.stream()).entrySet().forEach(e -> {
				System.out.println(e.getKey());
				e.getValue().forEach(n -> System.out.println("  " + n));
			});
		}
		
		// Now try with the a fixed size collection
		{
			List<String> list = new LinkedList<>();
			for (int index = 0; index < 4096; ++index) {
				list.add(Integer.toString(index));
			}
			
			find("10", list.stream()).entrySet().forEach(e -> {
				System.out.println(e.getKey());
				e.getValue().forEach(n -> System.out.println("  " + n));
			});
			
		}
	}
}
