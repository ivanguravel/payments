package org.ivzh.payments.executor;

import org.ivzh.payments.model.transaction.Transaction;
import org.ivzh.payments.model.transaction.TransactionBuilder;
import org.ivzh.payments.model.transaction.TransactionState;
import org.ivzh.payments.repository.AccountRepository;
import org.ivzh.payments.repository.TransactionRepository;
import org.slf4j.Logger;

/**
 * @author ivzh
 */
abstract class AbstractTransactionExecutor implements TransactionExecutor {

  private final AccountRepository accountRepository;
  private final TransactionRepository transactionRepository;

  public AbstractTransactionExecutor(AccountRepository accountRepository,
                                     TransactionRepository transactionRepository) {
    this.accountRepository = accountRepository;
    this.transactionRepository = transactionRepository;
  }

  @Override
  public final void execute(Transaction transaction) {
    if (apply(transaction)) {
      logger().debug("Start processing transaction {}", transaction);
      initProcess(transaction);
      update(processedTransaction(transaction));
      logger().info("Transaction {} processed", transaction);
    } else {
      throw new UnsupportedOperationException(String.format("Invalid executor for transaction %s", transaction));
    }
  }

  protected abstract Logger logger();

  protected abstract Transaction process(Transaction transaction);

  private Transaction processedTransaction(Transaction transaction) {
    try {
      return process(transaction);
    } catch (Exception ex) {
      logger().error(String.format("Error on processing transaction %s", transaction), ex);
      return transaction(transaction, TransactionState.ERROR)
          .errorMessage(ex.getMessage())
          .build();
    }
  }

  private void update(Transaction transaction) {
    transactionRepository.update(transaction);
    accountRepository.update(transaction.getSource());
    transaction.getDestination().ifPresent(accountRepository::update);
  }

  private void initProcess(Transaction transaction) {
    transaction.initProcess();
    transactionRepository.update(transaction);
  }

  protected TransactionBuilder transaction(Transaction transaction, TransactionState state) {
    return TransactionBuilder.create()
        .state(state)
        .id(transaction.getId())
        .type(transaction.getType())
        .date(transaction.getDate())
        .source(transaction.getSource())
        .ammount(transaction.getAmmount())
        .destination(transaction.getDestination());
  }
}
