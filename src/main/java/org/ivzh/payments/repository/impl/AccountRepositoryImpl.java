package org.ivzh.payments.repository.impl;

import org.ivzh.payments.model.account.Account;
import org.ivzh.payments.repository.AccountRepository;

import javax.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author ivzh
 */
@Singleton
class AccountRepositoryImpl implements AccountRepository {

    private final AtomicLong currentId;
    private final Map<Long, Account> accounts;

    public AccountRepositoryImpl() {
        this.currentId = new AtomicLong(0L);
        this.accounts = new ConcurrentHashMap<>();
    }

    @Override
    public Optional<Account> findById(long id) {
        return Optional.ofNullable(this.accounts.get(id));
    }

    @Override
    public Account save(Account account) {
        validateDuplicated(account);
        Long id = currentId.incrementAndGet();
        Account newAccount = Account.from(id, account.getNumber());
        accounts.putIfAbsent(id, newAccount);
        return newAccount;
    }

    @Override
    public List<Account> findAll() {
        return accounts.values()
                .stream()
                .map(Account::from)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Account> findByNumber(String number) {
        Account account = new Account(number);
        return accounts.values()
                .stream()
                .filter(a -> a.equals(account))
                .map(Account::from)
                .findFirst();
    }

    @Override
    public void update(Account account) {
        validateUpdate(account);
        accounts.replace(account.getId(), account);
    }

    private void validateDuplicated(Account account) {
        Predicate<Account> predicate = a -> a.equals(account);
        if (accounts.values().stream().anyMatch(predicate)) {
            throw new UnsupportedOperationException("Account already exists");
        }
    }

    private void validateUpdate(Account account) {
        Account existent = accounts.get(account.getId());
        if (!existent.equals(account)) {
            throw new UnsupportedOperationException("Unable to update account number");
        }
    }

}
