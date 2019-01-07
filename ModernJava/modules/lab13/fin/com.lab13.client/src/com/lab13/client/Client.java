package com.lab13.client;

import com.lab13.database.api.Durability;
import com.lab13.urlfetcher.api.UrlFetcher;
import com.lab13.urlfetcher.api.UrlFetcherFactory;

public class Client {

    public static void main(String... args) {
        UrlFetcher fetcher = UrlFetcherFactory.getFetcher();

        System.out.println("===================================");
        fetcher.getPage("https://www.google.com", Durability.SHORT).forEach(System.out::println);

        System.out.println("===================================");
        fetcher.getPage("https://www.google.com", Durability.SHORT).forEach(System.out::println);

        System.out.println("===================================");
        fetcher.getPage("https://www.wikipedia.org", Durability.LONG).forEach(System.out::println);

        System.out.println("===================================");
        fetcher.getPage("https://www.wikipedia.org", Durability.LONG).forEach(System.out::println);
    }
}
