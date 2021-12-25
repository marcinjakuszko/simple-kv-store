package dev.mrtn88.simplekvstore.store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class KeyValueStoreIntTest implements DataCommandsReceiver<String, Integer>, TransactionCommandsReceiver {

    private KeyValueStore<String, Integer> keyValueStore;

    @BeforeEach
    void setUp() {
        keyValueStore = new KeyValueStore<>();
    }

    @Test
    void scenario1() {
        begin();
        set("a", 10);
        assertEquals(10, get("a"));
        begin();
        set("a", 20);
        assertEquals(20, get("a"));
        rollback();
        assertEquals(10, get("a"));
        rollback();
        assertNull(get("a"));
    }

    @Test
    void scenario2() {
        begin();
        set("a", 30);
        begin();
        set("a", 40);
        commit();
        assertEquals(40, get("a"));
        assertThrows(IllegalStateException.class, this::rollback);
    }

    @Test
    void scenario3() {
        set("a", 50);
        begin();
        assertEquals(50, get("a"));
        set("a", 60);
        begin();
        delete("a");
        assertNull(get("a"));
        rollback();
        assertEquals(60, get("a"));
        commit();
        assertEquals(60, get("a"));
    }

    @Test
    void scenario4() {
        set("a", 10);
        begin();
        assertEquals(1, count(10));
        begin();
        delete("a");
        assertEquals(0, count(10));
        rollback();
        assertEquals(1, count(10));
    }

    @Test
    void scenario5() {
        set("a", 5);
        set("a", 10);
        begin();
        set("a", 20);
        assertEquals(1, count(20));
        rollback();
        assertThrows(IllegalStateException.class, this::rollback);
        assertEquals(10, get("a"));
    }

    @Override
    public void set(String key, Integer value) {
        keyValueStore.set(key, value);
    }

    @Override
    public Integer get(String key) {
        return keyValueStore.get(key);
    }

    @Override
    public void delete(String key) {
        keyValueStore.delete(key);
    }

    @Override
    public Integer count(Integer value) {
        return keyValueStore.count(value);
    }

    @Override
    public void begin() {
        keyValueStore.begin();
    }

    @Override
    public void commit() {
        keyValueStore.commit();
    }

    @Override
    public void rollback() {
        keyValueStore.rollback();
    }
}
