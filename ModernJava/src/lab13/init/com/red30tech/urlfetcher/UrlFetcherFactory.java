package lab21.init.com.red30tech.urlfetcher;

public class UrlFetcherFactory {

    public static UrlFetcher getFetcher() {
        return new CachedUrlFetcher();
    }
}
