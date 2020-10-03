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

/**
 * @author ivzh
 */
@Singleton
class CreditExecutor extends AbstractTransactionExecutor {

  private static final Logger LOGGER = LoggerFactory.getLogger(CreditExecutor.class);

  public CreditExecutor(AccountRepository accountRepository,
                        TransactionRepository repository) {
    super(accountRepository, repository);
  }

  @Override
  protected Transaction process(Transaction transaction) {
    LOGGER.info("Executing debit transaction {}", transaction);
    Account account = transaction.getSource();
    account.credit(transaction.getAmmount());
    return transaction(transaction, TransactionState.DONE).build();
  }

  @Override
  protected Logger logger() {
    return LOGGER;
  }

  @Override
  public boolean apply(Transaction transaction) {
    return TransactionType.CREDIT.equals(transaction.getType());
  }
}
