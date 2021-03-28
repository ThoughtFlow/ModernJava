package com.lab13.urlfetcher.impl;

import com.lab13.database.api.Database;
import com.lab13.database.api.DatabaseFactory;
import com.lab13.cache.api.Cache;
import com.lab13.database.api.Durability;
import com.lab13.urlfetcher.api.UrlFetcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CachedUrlFetcher implements UrlFetcher {

    private final Database<List<String>> database;

    public CachedUrlFetcher() {
        Database<List<String>> database = DatabaseFactory.getDatabase();

        try {
            database = new Cache<List<String>>(database);
            System.out.println("Cache found: Using cache and database");
        }
        catch (NoClassDefFoundError exception) {
            // We'll just use the regular database
            System.out.println("Cache not found: Using database only");
        }

        this.database = database;
    }

    @Override
    public Stream<String> getPage(String url, Durability durability) {
        Stream<String> stream;

        if (database.get(url) == null) {
            System.out.println("Not found in cache");
            try {
                stream = getReader(url).lines();
            } catch (IOException e) {
                stream = Stream.empty();
            }
            database.create(url, stream.collect(Collectors.toList()), durability);
        }
        else {
            System.out.println("Found in cache");
        }

        List<String> list = database.get(url);
        return list.stream();
    }

    private BufferedReader getReader(String stringedUrl) throws IOException {

        URL url = new URL(stringedUrl);
        return new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
    }
}
