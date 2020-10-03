package org.ivzh.payments.executor;

import org.ivzh.payments.model.transaction.Transaction;

import javax.inject.Singleton;
import java.util.List;

/**
 * @author ivzh
 */
@Singleton
public class ExecutorManager {

  private final List<TransactionExecutor> executors;

  public ExecutorManager(List<TransactionExecutor> executors) {
    this.executors = executors;
  }

  public void execute(Transaction transaction) {
    executor(transaction).execute(transaction);
  }

  private TransactionExecutor executor(final Transaction transaction) {
    return executors.stream()
        .filter(executor -> executor.apply(transaction))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException(String.format("No such transaction executor for transaction type %s", transaction.getType().name())));
  }
}
