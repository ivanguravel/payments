package org.ivzh.payments.executor;

import org.ivzh.payments.model.transaction.Transaction;

/**
 * @author ivzh
 */
interface TransactionExecutor {

  void execute(Transaction transaction);

  boolean apply(Transaction transaction);

}
