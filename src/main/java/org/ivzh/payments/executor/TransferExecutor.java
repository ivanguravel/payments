package org.ivzh.payments.executor;

import org.ivzh.payments.model.account.Account;
import org.ivzh.payments.model.transaction.Transaction;
import org.ivzh.payments.model.transaction.TransactionState;
import org.ivzh.payments.model.transaction.TransactionType;
import org.ivzh.payments.repository.AccountRepository;
import org.ivzh.payments.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Optional;

/**
 * @author ivzh
 */
@Singleton
class TransferExecutor extends AbstractTransactionExecutor {

  private static final Logger LOGGER = LoggerFactory.getLogger(Transaction.class);

  public TransferExecutor(AccountRepository accountRepository,
                          TransactionRepository transactionRepository) {
    super(accountRepository, transactionRepository);
  }

  @Override
  public boolean apply(Transaction transaction) {
    return TransactionType.TRANSFER.equals(transaction.getType());
  }

  @Override
  protected Transaction process(Transaction transaction) {
    LOGGER.info("Executing transfer transaction {}", transaction);
    debit(transaction);
    credit(transaction);
    return transaction(transaction, TransactionState.DONE).build();
  }

  private void credit(Transaction transaction) {
    Optional<Account> destination = transaction.getDestination();
    if (destination.isPresent()) {
      destination.get().credit(transaction.getAmmount());
    } else {
      throw new RuntimeException("Destination account is missing");
    }
  }

  private void debit(Transaction transaction) {
    Account source = transaction.getSource();
    source.debit(transaction.getAmmount());
  }

  @Override
  protected Logger logger() {
    return LOGGER;
  }
}
