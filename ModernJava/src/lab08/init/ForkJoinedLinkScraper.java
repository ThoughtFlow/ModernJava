package lab08.init;

import lab.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveTask;

public class ForkJoinedLinkScraper {

	/**
	 * Use Fork/Join RecursiveTask to split the work of scraping the HREFs from the list.
	 */
	private static class LinkScrapeTask extends RecursiveTask<List<String>> {

		private final List<String> urls;

		private LinkScrapeTask(List<String> urls) {
			// Add any other parameters
			this.urls = urls;
		}

		@Override
		protected List<String> compute() {

			// Implement this: Do forking and joining here
			List<String> scrapedUrls = new ArrayList<>();
			for (String nextUrl : urls) {
				scrapedUrls.addAll(Util.scrapeHrefs(nextUrl));
			}

			return scrapedUrls;
		}
	}

	/**
	 * Fork/Join RecursiveTask to split the work of cataloging the the occurences of each href.
	 */
	private static class LinkCatalogTask extends RecursiveTask<Map<String, Integer>> {
		private final List<String> hrefs;

		private LinkCatalogTask(List<String> hrefs) {
			// @todo Add any other parameters
			this.hrefs = hrefs;
		}

		@Override
		protected Map<String, Integer> compute() {

			Map<String, Integer> catalog;

			// todo Implement this: Do forking and joining here
			catalog = Util.catalog(hrefs);

			// Use merge to merge all forked catalogs into 1
			//catalog = Util.merge();

			return catalog;
		}

		/**
		 * Invokes the two Fork/Join tasks sequentially.
		 *
		 * @param urls A list of urls to scrape.
		 * @return A map of scraped urls for all sites and their occurrence count.
		 */
		private static Map<String, Integer> invoke(List<String> urls) {

			List<String> hrefs = new LinkScrapeTask(urls).compute();
			return new LinkCatalogTask(hrefs).compute();
		}

		public static void main(String... args) {

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

			Map<String, Integer> results = invoke(urls);

			System.out.println("Scraped links and count: ");
			results.entrySet().forEach(e -> System.out.println(" - " + e.getKey() + ": " + e.getValue()));
		}
	}
}