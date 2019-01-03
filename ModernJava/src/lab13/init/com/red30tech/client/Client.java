package lab21.init.com.red30tech.client;

import lab21.init.com.red30tech.database.Durability;
import lab21.init.com.red30tech.urlfetcher.UrlFetcher;
import lab21.init.com.red30tech.urlfetcher.UrlFetcherFactory;

public class Client {

    public static void main(String... args) {
        UrlFetcher fetcher = UrlFetcherFactory.getFetcher();

        fetcher.getPage("https://www.google.com", Durability.SHORT).forEach(System.out::println);
        fetcher.getPage("https://www.google.com", Durability.SHORT).forEach(System.out::println);
        fetcher.getPage("https://www.wikipedia.org", Durability.LONG).forEach(System.out::println);
        fetcher.getPage("https://www.wikipedia.org", Durability.LONG).forEach(System.out::println);
    }
}
