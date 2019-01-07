package com.lab13.database.impl;

import java.util.Map;
import java.util.HashMap;

import com.lab13.database.api.Database;
import com.lab13.database.api.Durability;

public class InMemoryDatabase<T> implements Database<T> {

    private final Map<String, T> database;

    public InMemoryDatabase() {
        database = new HashMap<>();
    }

    @Override
    public void create(String key, T value, Durability durability) {
        // Ignoring the durability parameter. Presumably, we could archive this data in a different store.
        database.put(key, value);
    }

    @Override
    public T get(String key) {
        return database.get(key);
    }

    @Override
    public void update(String key, T value) {
        database.put(key, value);
    }

    @Override
    public void delete(String key) {
        database.remove(key);
    }
}