package dev.mrtn88.simplekvstore.store;

import dev.mrtn88.simplekvstore.store.storage.Storage;
import dev.mrtn88.simplekvstore.store.transaction.TransactionManager;

import java.util.HashMap;
import java.util.Optional;

public class KeyValueStore<K,V> implements DataCommandsReceiver<K, V>, TransactionCommandsReceiver {

    private final Storage<K, V> storage = new Storage<>(new HashMap<>(), new HashMap<>());
    private final TransactionManager<K, V> transactionManager = new TransactionManager<>(storage);

    public KeyValueStore() {
    }

    @Override
    public void begin() {
        transactionManager.begin();
    }

    @Override
    public void commit() {
        transactionManager.commit();
    }

    @Override
    public void rollback() {
        transactionManager.rollback();
    }

    @Override
    public void set(K key, V value) {
        getDataCommandsReceiver().set(key, value);
    }

    @Override
    public V get(K key) {
        return getDataCommandsReceiver().get(key);
    }

    @Override
    public void delete(K key) {
        getDataCommandsReceiver().delete(key);
    }

    @Override
    public Integer count(V value) {
        return getDataCommandsReceiver().count(value);
    }

    private DataCommandsReceiver<K,V> getDataCommandsReceiver() {
        return Optional.ofNullable(transactionReceiver()).orElse(storageReceiver());
    }

    private DataCommandsReceiver<K,V> transactionReceiver() {
        return transactionManager.getActiveTransaction();
    }

    private DataCommandsReceiver<K,V> storageReceiver() {
        return storage;
    }
}
