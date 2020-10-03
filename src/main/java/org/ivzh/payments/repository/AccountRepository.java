package org.ivzh.payments.repository;

import org.ivzh.payments.model.account.Account;

import java.util.List;
import java.util.Optional;

/**
 * @author ivzh
 */
public interface AccountRepository {

  Optional<Account> findById(long id);

  Account save(Account account);

  List<Account> findAll();

  Optional<Account> findByNumber(String number);

  void update(Account account);
}