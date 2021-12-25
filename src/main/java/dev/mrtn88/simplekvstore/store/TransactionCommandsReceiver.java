package dev.mrtn88.simplekvstore.store;

public interface TransactionCommandsReceiver {

    void begin();

    void commit();

    void rollback();
}
