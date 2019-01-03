package lab21.init.com.red30tech.urlfetcher;

import lab21.init.com.red30tech.database.Durability;

import java.util.stream.Stream;

public interface UrlFetcher {

    public Stream<String> getPage(String url, Durability durability);
}
