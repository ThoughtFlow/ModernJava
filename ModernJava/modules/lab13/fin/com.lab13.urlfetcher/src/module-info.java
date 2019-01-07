module com.lab13.urlfetcher {
    requires transitive com.lab13.database;
    requires static com.lab13.cache;
    exports com.lab13.urlfetcher.api;
}
