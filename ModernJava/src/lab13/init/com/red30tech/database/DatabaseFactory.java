package lab21.init.com.red30tech.database;

public class DatabaseFactory {

   public static Database getDatabase() {
      return new InMemoryDatabase();
   }
}
