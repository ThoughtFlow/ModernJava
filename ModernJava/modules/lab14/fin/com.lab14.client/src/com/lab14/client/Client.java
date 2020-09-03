package com.lab14.client;

import com.lab14.database.api.Durability;
import com.lab14.urlfetcher.api.UrlFetcher;
import com.lab14.urlfetcher.api.UrlFetcherFactory;

public class Client {

    public static void main(String... args) {
        UrlFetcher fetcher = UrlFetcherFactory.getFetcher();

        System.out.println("===================================");
        fetcher.getPage("http://www.google.com", Durability.SHORT).forEach(System.out::println);

        System.out.println("===================================");
        fetcher.getPage("http://www.google.com", Durability.SHORT).forEach(System.out::println);

        System.out.println("===================================");
        fetcher.getPage("http://www.wikipedia.org", Durability.LONG).forEach(System.out::println);

        System.out.println("===================================");
        fetcher.getPage("http://www.wikipedia.org", Durability.LONG).forEach(System.out::println);
    }
}
