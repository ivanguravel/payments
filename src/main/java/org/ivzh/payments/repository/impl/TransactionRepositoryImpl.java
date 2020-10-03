package org.ivzh.payments.repository.impl;

import org.ivzh.payments.model.account.Account;
import org.ivzh.payments.model.transaction.Transaction;
import org.ivzh.payments.model.transaction.TransactionBuilder;
import org.ivzh.payments.repository.TransactionRepository;

import javax.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @author ivzh
 */
@Singleton
class TransactionRepositoryImpl implements TransactionRepository {

    private final AtomicLong currentId;
    private final Map<Long, Transaction> transactions;

    public TransactionRepositoryImpl() {
        currentId = new AtomicLong(0L);
        transactions = new ConcurrentHashMap<>();
    }

    @Override
    public Transaction save(Transaction transaction) {
        validateDuplicated(transaction);
        Long id = currentId.incrementAndGet();
        Transaction newTransaction = TransactionBuilder.create().id(id).build(transaction, true);
        transactions.putIfAbsent(id, newTransaction);
        return newTransaction;
    }

    @Override
    public List<Transaction> findAll() {
        return transactions.values()
                .stream()
                .map(this::newTransaction)
                .collect(Collectors.toList());
    }

    @Override
    public void update(Transaction transaction) {
        validateUpdate(transaction);
        transactions.replace(transaction.getId(), transaction);
    }

    @Override
    public Optional<Transaction> findById(long id) {
        Transaction transaction = transactions.get(id);
        return transaction != null
                ? Optional.of(newTransaction(transaction))
                : Optional.empty();
    }

    @Override
    public List<Transaction> findAllByAccount(Account account) {
        return transactions.values()
                .stream()
                .filter(transaction -> transaction.contains(account))
                .map(this::newTransaction)
                .collect(Collectors.toList());
    }

    private Transaction newTransaction(Transaction transaction) {
        return TransactionBuilder.create().build(transaction);
    }

    private void validateDuplicated(Transaction transaction) {
        if (transactions.values().stream().anyMatch(t -> t.equals(transaction))) {
            throw new UnsupportedOperationException("Transaction duplicated");
        }
    }

    private void validateUpdate(Transaction transaction) {
        Transaction existent = transactions.get(transaction.getId());
        if (!existent.equals(transaction)) {
            throw new UnsupportedOperationException("Unable to update a transaction field other than state");
        }
    }

}
