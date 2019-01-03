package com.red30tech.database.impl;

import java.util.Map;
import java.util.HashMap;

import com.red30tech.database.api.Database;
import com.red30tech.database.api.Durability;

public class InMemoryDatabase implements Database {

    private final Map<String, Object> database;

    public InMemoryDatabase() {
        database = new HashMap<>();
    }

    @Override
    public void create(String key, Object value, Durability durability) {
        // Ignoring the durability parameter. Presumably, we could archive this data in a different store.
        database.put(key, value);
    }

    @Override
    public Object get(String key) {
        return database.get(key);
    }

    @Override
    public void update(String key, Object value) {
        database.put(key, value);
    }

    @Override
    public void delete(String key) {
        database.remove(key);
    }
}
