package dev.mrtn88.simplekvstore.store;

public interface DataCommandsReceiver<K, V> {

    void set(K key, V value);

    V get(K key);

    void delete(K key);

    Integer count(V value);
}
