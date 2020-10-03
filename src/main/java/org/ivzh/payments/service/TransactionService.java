package org.ivzh.payments.service;

import org.ivzh.payments.api.dto.TransactionDto;
import org.ivzh.payments.command.transaction.TransactionSaveCommand;
import org.ivzh.payments.executor.ExecutorManager;
import org.ivzh.payments.model.account.Account;
import org.ivzh.payments.model.transaction.Transaction;
import org.ivzh.payments.model.transaction.TransactionBuilder;
import org.ivzh.payments.repository.AccountRepository;
import org.ivzh.payments.repository.TransactionRepository;

import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ivzh
 */
@Singleton
public class TransactionService {

  private final ExecutorManager executorManager;
  private final AccountRepository accountRepository;
  private final TransactionRepository transactionRepository;

  public TransactionService(AccountRepository accountRepository,
                            TransactionRepository transactionRepository,
                            ExecutorManager executorManager) {
    this.executorManager = executorManager;
    this.accountRepository = accountRepository;
    this.transactionRepository = transactionRepository;
  }

  public Transaction create(TransactionSaveCommand command) {
    Transaction transaction = transactionRepository.save(transaction(command));
    executorManager.execute(transaction);
    return transaction;
  }

  public List<TransactionDto> fromAccount(long id) {
    Account account = accountRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Account does not exist"));
    return transactionRepository.findAllByAccount(account)
        .stream()
        .map(TransactionDto::new)
        .collect(Collectors.toList());
  }

  public TransactionDto findById(long id) {
    Transaction transaction = transactionRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException(String.format("Transaction %s not found", id)));
    return new TransactionDto(transaction);
  }

  private Transaction transaction(final TransactionSaveCommand command) {
    Account sourceAccount = account(command.getSourceId()).orElseThrow(() -> new IllegalArgumentException("Account does not exist"));
    return TransactionBuilder.create()
        .source(sourceAccount)
        .type(command.getType())
        .ammount(command.getAmmount())
        .destination(account(command.getDestinationId()))
        .build();
  }

  private Optional<Account> account(long id) {
    return accountRepository.findById(id);
  }

}
