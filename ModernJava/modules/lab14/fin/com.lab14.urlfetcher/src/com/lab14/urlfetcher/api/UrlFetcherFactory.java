package com.lab14.urlfetcher.api;

import com.lab14.urlfetcher.impl.CachedUrlFetcher;

public class UrlFetcherFactory {

    public static UrlFetcher getFetcher() {
        return new CachedUrlFetcher();
    }
}
