package com.lab13.urlfetcher.api;

import com.lab13.urlfetcher.impl.CachedUrlFetcher;

public class UrlFetcherFactory {

    public static UrlFetcher getFetcher() {
        return new CachedUrlFetcher();
    }
}
