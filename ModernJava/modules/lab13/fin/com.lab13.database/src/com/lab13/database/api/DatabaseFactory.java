package com.lab13.database.api;

import com.lab13.database.impl.InMemoryDatabase;

public class DatabaseFactory {

   public static <T> Database<T> getDatabase() {

      return new InMemoryDatabase<T>();
   }
}
