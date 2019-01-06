package lab13.init.client;

import lab13.init.database.Durability;
import lab13.init.urlfetcher.UrlFetcher;
import lab13.init.urlfetcher.UrlFetcherFactory;

public class Client {

    public static void main(String... args) {
        UrlFetcher fetcher = UrlFetcherFactory.getFetcher();

        fetcher.getPage("https://www.google.com", Durability.SHORT).forEach(System.out::println);
        fetcher.getPage("https://www.google.com", Durability.SHORT).forEach(System.out::println);
        fetcher.getPage("https://www.wikipedia.org", Durability.LONG).forEach(System.out::println);
        fetcher.getPage("https://www.wikipedia.org", Durability.LONG).forEach(System.out::println);
    }
}
