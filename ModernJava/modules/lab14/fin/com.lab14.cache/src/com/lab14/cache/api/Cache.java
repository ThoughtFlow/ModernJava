package com.lab14.cache.api;

import com.lab14.database.api.Database;
import com.lab14.database.api.Durability;

import java.util.HashMap;
import java.util.Map;

public class Cache<T> implements Database<T> {

    private final Map<String, T> cache = new HashMap<>();
    private final Database<T> backendDatabase;

    public Cache(Database<T> database) {
        backendDatabase = database;
    }

    @Override
    public void create(String key, T value, Durability durability) {
        // We only cache long-lived data
        if (durability == Durability.LONG) {
            cache.put(key, value);
        }

        backendDatabase.create(key, value, durability);
    }

    @Override
    public T get(String key) {
        return cache.containsKey(key) ? cache.get(key) : backendDatabase.get(key);
    }

    @Override
    public void update(String key, T value) {
        if (cache.containsKey(key)) {
            cache.put(key, value);
        }

        backendDatabase.update(key, value);
    }

    @Override
    public void delete(String key) {
        cache.remove(key);
        backendDatabase.delete(key);
    }
}

