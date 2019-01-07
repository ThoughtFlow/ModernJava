package lab13.init.urlfetcher;

import lab13.init.database.Durability;

import java.util.stream.Stream;

public interface UrlFetcher {

    public Stream<String> getPage(String url, Durability durability);
}
