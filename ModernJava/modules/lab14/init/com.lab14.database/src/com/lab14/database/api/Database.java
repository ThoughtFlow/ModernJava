package com.lab14.database.api;

public interface Database<T> {

   public void create(String key, T value, Durability durability);

   public T get(String key);

   public void update(String key, T value);

   public void delete(String key);
}
