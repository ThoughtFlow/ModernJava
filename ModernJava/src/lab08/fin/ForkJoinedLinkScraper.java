package lab08.fin;

import lab.util.Util;

import java.util.*;
import java.util.concurrent.*;

public class ForkJoinedLinkScraper {

	/**
	 * Fork/Join RecursiveTask to split the work of scraping the HREFs from the URLs.
	 *
	 * Will Fork/Join until the list to scrape is <= OPTIMAL_SCRAPE_SIZE. Will merge all HREFs into a single list and
	 * return that list.
	 */
	private static class LinkScrapeTask extends RecursiveTask<List<String>> {

		private static final int OPTIMAL_SCRAPE_SIZE = 1;

		private final List<String> urls;
		private final int startIndex;
		private final int endIndexInclusive;


		private LinkScrapeTask(List<String> urls, int startIndex, int endIndexInclusive) {
			this.urls = urls;
			this.startIndex = startIndex;
			this.endIndexInclusive = endIndexInclusive;
		}

		@Override
		protected List<String> compute() {
			List<String> scrapedUrls;
			int range = endIndexInclusive - startIndex;

			if (range > OPTIMAL_SCRAPE_SIZE) {
				int halfIndex = range / 2 + startIndex;
				LinkScrapeTask firstHalf  = new LinkScrapeTask(urls, startIndex, halfIndex);
				LinkScrapeTask secondHalf  = new LinkScrapeTask(urls, halfIndex + 1, endIndexInclusive);
				firstHalf.fork();
				secondHalf.fork();

				scrapedUrls = firstHalf.join();
				scrapedUrls.addAll(secondHalf.join());
			}
			else {
				scrapedUrls = new ArrayList<>();
				for (int index = startIndex; index <= endIndexInclusive; ++index) {
					scrapedUrls.addAll(Util.scrapeHrefs(urls.get(index)));
				}
			}

			return scrapedUrls;
		}
	}

	/**
	 * Fork/Join RecursiveTask to split the work of cataloging the the occurences of each href.
	 *
	 * Will Fork/Join until the list to catalog is <= OPTIMAL_SCRAPE_SIZE. Will start with a List of HREFs as strings
	 * and return a catalog as a Map of Strings (representing the HREFs and the Integer (representing the count).
	 */
	private static class LinkCatalogTask extends RecursiveTask<Map<String, Integer>>
	{
		private static final int OPTIMAL_SCRAPE_SIZE = 25;

		private final List<String> lines;
		private final int startIndex;
		private final int endIndexExclusive;

		private LinkCatalogTask(List<String> lines, int startIndex, int endIndexExclusive) {
			this.lines = lines;
			this.startIndex = startIndex;
			this.endIndexExclusive = endIndexExclusive;
		}

		@Override
		protected Map<String, Integer> compute() {
			Map<String, Integer> catalog;
			int range = endIndexExclusive - startIndex;

			if (range > OPTIMAL_SCRAPE_SIZE) {
				int halfIndex = range / 2 + startIndex;
				LinkCatalogTask firstHalf  = new LinkCatalogTask(lines, startIndex, halfIndex);
				LinkCatalogTask secondHalf  = new LinkCatalogTask(lines, halfIndex + 1, endIndexExclusive);
				firstHalf.fork();
				secondHalf.fork();

				Map<String, Integer> map1 = firstHalf.join();
				Map<String, Integer> map2 = secondHalf.join();
				catalog = Util.merge(map1, map2);
			}
			else {
				catalog = catalog(lines.subList(startIndex, endIndexExclusive));
			}

			return catalog;
		}
	}

	/**
	 * Invokes the two Fork/Join tasks sequentially.
	 * 
	 * @param urls A list of urls to scrape.
	 * @return A map of scraped urls for all sites and their occurrence count.
	 */
	private static Map<String, Integer> invoke(List<String> urls) throws ExecutionException, InterruptedException {

		ForkJoinPool executor = ForkJoinPool.commonPool();
		ForkJoinTask<List<String>> hrefs = executor.submit(new LinkScrapeTask(urls, 0, urls.size() - 1));

		// This will block until the hrefs.get() is ready
		ForkJoinTask<Map<String, Integer>> catalog = executor.submit(new LinkCatalogTask(hrefs.get(), 0, hrefs.get().size()));
		executor.shutdown();

		// This will block until the catalog.get() is ready
		return catalog.get();
	}

	private static Map<String, Integer> catalog(List<String> stringsToCatalogue) {

		Map<String, Integer> map = new ConcurrentHashMap<>();

		stringsToCatalogue.stream().forEach(s -> {
			map.merge(s, 1, (v1, v2) -> ++v1);
		});

		return map;
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

		try {
			Map<String, Integer> results = invoke(urls);

			System.out.println("Scraped links and count: ");
			results.entrySet().forEach(e -> System.out.println(" - " + e.getKey() + ": " + e.getValue()));
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
}