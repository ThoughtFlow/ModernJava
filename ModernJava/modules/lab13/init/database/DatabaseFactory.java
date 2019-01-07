package lab13.init.database;

public class DatabaseFactory {

    public static <T> Database<T> getDatabase() {

        return new InMemoryDatabase<T>();
    }
}
