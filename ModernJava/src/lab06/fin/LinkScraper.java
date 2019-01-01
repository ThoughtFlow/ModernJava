package lab06.fin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import lab.util.Util;

public class LinkScraper {

	private static class ScraperWorker implements Callable<Map<String, Integer>> {

		final String url;
		 
		public ScraperWorker(String url) {
			this.url = url;
		}

		/**
		 * Scrapes off href links of the given url and returns a Map of urls and their occurrences.
		 * 
		 * @return A map of scrapped urls and their occurrence count.
		 */
		@Override
		public Map<String, Integer> call() {
			
			List<String> hrefs = Util.scrapeHrefs(url);
			return Util.catalog(hrefs);
		}
	}
	
	/**
	 * Starts the thread pool and creates a scraperWorker for each site to scrape.
	 * 
	 * @param urls A list of urls to scrape.
	 * @return A map of scraped urls for all sites and their occurrence count.
	 * @throws InterruptedException Thrown upon a thread error.
	 * @throws ExecutionException Thrown upon an error in the callable.
	 */
	private static Map<String, Integer> invoke(List<String> urls) throws InterruptedException, ExecutionException {
		
		Map<String, Integer> mergedMap = new HashMap<>();
		ExecutorService pool = Executors.newFixedThreadPool(10);

		List<Callable<Map<String, Integer>>> scraperWorkers = new ArrayList<>();
		for (String nextUrl : urls) {
			scraperWorkers.add(new ScraperWorker(nextUrl));
		}

		List<Future<Map<String, Integer>>> futures;
		try {
			futures = pool.invokeAll(scraperWorkers);
			
			for (Future<Map<String, Integer>> nextFuture : futures) {
				mergedMap = Util.merge(mergedMap, nextFuture.get());
			}
		} 
		finally {
			// Make sure to close the pool no matter what.
			pool.shutdown();
		}
		
		return mergedMap;
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
