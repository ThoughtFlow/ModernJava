package com.lab13.urlfetcher.api;

import com.lab13.database.api.Durability;

import java.util.stream.Stream;

public interface UrlFetcher {

    public Stream<String> getPage(String url, Durability durability);
}