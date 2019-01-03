package com.red30tech.client;

import com.red30tech.database.api.Durability;
import com.red30tech.urlfetcher.api.UrlFetcher;
import com.red30tech.urlfetcher.api.UrlFetcherFactory;

public class Client {

    public static void main(String... args) {
        UrlFetcher fetcher = UrlFetcherFactory.getFetcher();

        fetcher.getPage("https://www.google.com", Durability.SHORT).forEach(System.out::println);
        fetcher.getPage("https://www.google.com", Durability.SHORT).forEach(System.out::println);
        fetcher.getPage("https://www.wikipedia.org", Durability.LONG).forEach(System.out::println);
        fetcher.getPage("https://www.wikipedia.org", Durability.LONG).forEach(System.out::println);
    }
}
