package dev.mrtn88.simplekvstore.store.transaction;

import dev.mrtn88.simplekvstore.store.DataCommandsReceiver;
import dev.mrtn88.simplekvstore.store.storage.Storage;

import java.util.Optional;

public class TransactionContext<K,V> implements DataCommandsReceiver<K,V> {

    private final Storage<K,V> mainStorage;
    private final Storage<K, Optional<V>> transactionLog;
    private final TransactionContext<K,V> parentContext;

    public TransactionContext(
            Storage<K,V> mainStorage,
            Storage<K, Optional<V>> transactionLog,
            TransactionContext<K, V> parentContext) {
        this.mainStorage = mainStorage;
        this.transactionLog = transactionLog;
        this.parentContext = parentContext;
    }

    @Override
    public void set(K key, V value) {
        transactionLog.set(key, Optional.of(value));
    }

    @Override
    public V get(K key) {
         var value = transactionLog.get(key);
         if (value == null) {
             return getMainStorageIfEmptyParentContext().get(key);
         }
         return value.orElse(null);
    }

    @Override
    public void delete(K key) {
        var value = get(key);
        transactionLog.delete(key, Optional.of(value));
        transactionLog.set(key, Optional.empty());
    }

    @Override
    public Integer count(V value) {
        var count = transactionLog.count(Optional.of(value));
        if (parentContext == null) {
            return count + mainStorage.count(value);
        }
        return count + parentContext.count(value);
    }

    public void commit() {
        for (K key : transactionLog.getKeys()) {
            var keyValue = transactionLog.get(key);
            if (keyValue.isEmpty()) {
                mainStorage.delete(key);
            } else {
                mainStorage.set(key, keyValue.get());
            }
        }
    }

    private DataCommandsReceiver<K,V> getMainStorageIfEmptyParentContext() {
        if (parentContext == null) {
            return mainStorage;
        }
        return parentContext;
    }

}
