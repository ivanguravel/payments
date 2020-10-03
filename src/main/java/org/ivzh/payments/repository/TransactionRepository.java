package org.ivzh.payments.repository;

import org.ivzh.payments.model.account.Account;
import org.ivzh.payments.model.transaction.Transaction;

import java.util.List;
import java.util.Optional;

/**
 * @author ivzh
 */
public interface TransactionRepository {

  Transaction save(Transaction transaction);

  List<Transaction> findAll();

  void update(Transaction transaction);

  Optional<Transaction> findById(long id);

  List<Transaction> findAllByAccount(Account account);

}
