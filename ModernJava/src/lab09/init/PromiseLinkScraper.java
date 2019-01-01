package lab09.init;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

public class PromiseLinkScraper {

	
	/**
	 * Scrapes and catalogs URLs using promises.
	 * 
	 * @param urls A list of urls to scrape.
	 * @return A map of scraped urls for all sites and their occurrence count.
	 * @throws InterruptedException Thrown upon a thread error.
	 * @throws ExecutionException Thrown upon an error in the promise.
	 */
	private static Map<String, Integer> invoke(List<String> urls) throws InterruptedException, ExecutionException {
		
		// Implement this method as a promise
		// Use Util.scrapeHrefs(), Util.catalog() and Util.merge()
		
		return new ConcurrentHashMap<String, Integer>();
	}
	
	public static void main(String... args)  {
		
		List<String> urls = Arrays.asList(
										  "https://www.google.com",
										  "https://www.youtube.com",
										  "https://www.facebook.com",
								          "https://www.wikipedia.org",
										  "https://www.spotify.com",
										  "https://www.yahoo.com",
										  "https://www.linkedin.com",
										  "https://www.twitter.com",
										  "https://www.ebay.com",
										  "https://www.apple.com");
		
		try {
		   Map<String, Integer> results = invoke(urls);

		   for (String nextKey : results.keySet()) {
			   System.out.println(nextKey + ": " + results.get(nextKey));
		   }
		}
		catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
}
