package lab21.init.com.red30tech.database;

public interface Database {

   public void create(String key, Object value, Durability durability);

   public Object get(String key);

   public void update(String key, Object value);

   public void delete(String key);
}
