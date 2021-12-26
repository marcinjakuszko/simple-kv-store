package dev.mrtn88.simplekvstore.store.transaction;

import dev.mrtn88.simplekvstore.store.storage.Storage;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

public class TransactionLog<K, V>  extends Storage<K, Optional<V>> {

    public TransactionLog() {
         super(new HashMap<>(), new HashMap<>());
    }

    public void delete(K key, V value) {
        var deletedValue = keyValueMap.remove(key);
        if (deletedValue == null) {
            var counter = valueCountMap.getOrDefault(value, 0);
            valueCountMap.put(Optional.of(value), --counter);
        }
        keyValueMap.put(key, Optional.empty());
    }

    public Set<K> getKeys() {
        return keyValueMap.keySet();
    }
}
