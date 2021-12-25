package dev.mrtn88.simplekvstore.store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DataCommandsReceiverTest {

    private DataCommandsReceiver<String, Integer> dataCommandsReceiver;

    @BeforeEach
    void setUp() {
        dataCommandsReceiver = new KeyValueStore<>();
    }

    @Test
    void shouldSetValue() {
        dataCommandsReceiver.set("a", 10);

        assertEquals(10, dataCommandsReceiver.get("a"));
    }

    @Test
    void shuoldDeleteValue() {
        dataCommandsReceiver.set("a", 10);

        dataCommandsReceiver.delete("a");
        assertNull(dataCommandsReceiver.get("a"));
    }

    @Test
    void shouldCountSingleValue() {
        dataCommandsReceiver.set("a", 10);

        assertEquals(1, dataCommandsReceiver.count(10));
    }

    @Test
    void shouldCountMultipleValues() {
        dataCommandsReceiver.set("a", 10);
        dataCommandsReceiver.set("b", 10);
        dataCommandsReceiver.set("c", 10);

        assertEquals(3, dataCommandsReceiver.count(10));
    }

    @Test
    void shouldCountProperlyWhenDeleted() {
        dataCommandsReceiver.set("a", 10);
        dataCommandsReceiver.set("b", 10);
        dataCommandsReceiver.delete("a");

        assertEquals(1, dataCommandsReceiver.count(10));
    }

    @Test
    void shouldNotReturnNegativeValuesForCount() {
        dataCommandsReceiver.set("a", 10);
        dataCommandsReceiver.set("b", 10);
        dataCommandsReceiver.set("c", 10);
        dataCommandsReceiver.delete("a");
        dataCommandsReceiver.delete("b");
        dataCommandsReceiver.delete("c");

        assertEquals(0, dataCommandsReceiver.count(10));
    }

    @Test
    void shouldReturnZeroForNonExistingValueWhenCount() {
        assertEquals(0, dataCommandsReceiver.count(20));
    }
}
