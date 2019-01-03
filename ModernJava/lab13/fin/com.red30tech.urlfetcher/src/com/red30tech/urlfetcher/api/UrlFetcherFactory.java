package com.red30tech.urlfetcher.api;

import com.red30tech.urlfetcher.impl.CachedUrlFetcher;

public class UrlFetcherFactory {

    public static UrlFetcher getFetcher() {
        return new CachedUrlFetcher();
    }
}
