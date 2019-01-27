module com.lab14.urlfetcher {
    requires transitive com.lab14.database;
    requires static com.lab14.cache;
    exports com.lab14.urlfetcher.api;
}
