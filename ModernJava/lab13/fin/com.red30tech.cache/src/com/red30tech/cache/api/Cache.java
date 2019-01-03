package com.red30tech.cache.api;

import com.red30tech.database.api.Database;
import com.red30tech.database.api.Durability;

import java.util.HashMap;
import java.util.Map;

public class Cache implements Database {

   private final Map<String, Object> cache = new HashMap<>();
   private final Database backendDatabase;

   public Cache(Database database) {
      backendDatabase = database;
   }

   @Override
   public void create(String key, Object value, Durability durability) {
       // We only cache long-lived data
       if (durability == Durability.LONG) {
           cache.put(key, value);
       }

       backendDatabase.create(key, value, durability);
   }

   @Override
   public Object get(String key) {
       return cache.containsKey(key) ? cache.get(key) : backendDatabase.get(key);
   }

   @Override
   public void update(String key, Object value) {
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
