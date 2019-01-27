package com.lab14.database.api;

import com.lab14.database.impl.InMemoryDatabase;

public class DatabaseFactory {

   public static <T> Database<T> getDatabase() {

      return new InMemoryDatabase<T>();
   }
}
