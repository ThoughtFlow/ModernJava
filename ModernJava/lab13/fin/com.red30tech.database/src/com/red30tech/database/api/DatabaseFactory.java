package com.red30tech.database.api;

import com.red30tech.database.impl.InMemoryDatabase;

public class DatabaseFactory {

   public static Database getDatabase() {
      return new InMemoryDatabase();
   }
}
