package dev.mrtn88.simplekvstore.store.transaction;

import dev.mrtn88.simplekvstore.store.storage.Storage;
import dev.mrtn88.simplekvstore.store.TransactionCommandsReceiver;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;

public class TransactionManager<K,V> implements TransactionCommandsReceiver {

    private final Storage<K,V> storage;
    private final LinkedList<TransactionContext<K,V>> transactions;

    public TransactionManager(Storage<K,V> storage) {
        this.storage = storage;
        transactions = new LinkedList<>();
    }

    public TransactionContext<K, V> getActiveTransaction() {
        if (transactions.isEmpty()) {
            return null;
        }
        return transactions.getLast();
    }

    @Override
    public void begin() {
        if (transactions.isEmpty()) {
            transactions.add(newTransactionContext(storage, null));
        } else {
            transactions.add(newTransactionContext(storage, transactions.getLast()));
        }
    }

    @Override
    public void commit() {
        if (transactions.isEmpty()) {
            throw new IllegalStateException("NO TRANSACTION");
        }
        var iterator = transactions.iterator();
        while (iterator.hasNext()) {
            var currentTransaction = iterator.next();
            currentTransaction.commit();
            iterator.remove();
        }
    }

    @Override
    public void rollback() {
        if (transactions.isEmpty()) {
            throw new IllegalStateException("NO TRANSACTION");
        }
        transactions.removeLast();
    }

    private TransactionContext<K,V> newTransactionContext(Storage<K,V> storage, TransactionContext<K,V> parent) {
        return new TransactionContext<>(storage, new Storage<>(new HashMap<>(), new HashMap<>()), parent);
    }
}
