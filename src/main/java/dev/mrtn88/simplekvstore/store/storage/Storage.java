package dev.mrtn88.simplekvstore.store.storage;

import dev.mrtn88.simplekvstore.store.DataCommandsReceiver;

import java.util.Map;
import java.util.Set;

public class Storage<K, V> implements DataCommandsReceiver<K, V> {

    private final Map<K, V> keyValueMap;
    private final Map<V, Integer> valueCountMap;

    public Storage(Map<K, V> keyValueMap, Map<V, Integer> valueCountMap) {
        this.keyValueMap = keyValueMap;
        this.valueCountMap = valueCountMap;
    }

    @Override
    public void set(K key, V value) {
        var counter = valueCountMap.getOrDefault(value, 0);
        valueCountMap.put(value, ++counter);
        keyValueMap.put(key, value);
    }

    @Override
    public V get(K key) {
        return keyValueMap.get(key);
    }

    @Override
    public void delete(K key) {
        var value = keyValueMap.remove(key);
        if (value != null) {
            var counter = valueCountMap.getOrDefault(value, 0);
            if (counter <= 1) {
                valueCountMap.remove(value);
            } else {
                valueCountMap.put(value, --counter);
            }
        }
    }

    public void delete(K key, V value) {
        var deletedValue = keyValueMap.remove(key);
        if (deletedValue == null) {
            var counter = valueCountMap.getOrDefault(value, 0);
            valueCountMap.put(value, --counter);
        }
    }

    @Override
    public Integer count(V value) {
        return valueCountMap.getOrDefault(value, 0);
    }

    public Set<K> getKeys() {
        return keyValueMap.keySet();
    }
}
