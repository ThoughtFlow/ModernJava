package com.red30tech.urlfetcher.api;

import com.red30tech.database.api.Durability;

import java.util.stream.Stream;

public interface UrlFetcher {

    public Stream<String> getPage(String url, Durability durability);
}
