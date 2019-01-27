package com.lab14.urlfetcher.api;

import com.lab14.database.api.Durability;

import java.util.stream.Stream;

public interface UrlFetcher {

    public Stream<String> getPage(String url, Durability durability);
}