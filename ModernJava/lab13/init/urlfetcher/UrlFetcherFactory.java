package lab13.init.urlfetcher;

public class UrlFetcherFactory {

    public static UrlFetcher getFetcher() {
        return new CachedUrlFetcher();
    }
}
