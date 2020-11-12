package org.ivzh.payments.service;


import io.micronaut.context.ApplicationContext;
import org.ivzh.payments.Application;
import org.ivzh.payments.command.transaction.TransactionSaveCommand;
import org.ivzh.payments.model.account.Account;
import org.ivzh.payments.model.transaction.Transaction;
import org.ivzh.payments.repository.AccountRepository;
import org.ivzh.payments.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.ivzh.payments.model.transaction.TransactionState.DONE;
import static org.ivzh.payments.model.transaction.TransactionType.CREDIT;
import static org.ivzh.payments.model.transaction.TransactionType.DEBIT;

/**
 * @author ivzh
 */
class TransactionServiceTest {

    private Account source;
    private Account destination;

    private AccountRepository accountRepository;
    private TransactionService transactionService;
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        ApplicationContext context = ApplicationContext.build(Application.class).start();
        transactionRepository = context.getBean(TransactionRepository.class);
        transactionService = context.getBean(TransactionService.class);
        accountRepository = context.getBean(AccountRepository.class);

        source = accountRepository.save(new Account("1"));
        destination = accountRepository.save(new Account("2"));
    }

    @Test
    @DisplayName("credit transaction finished without errors")
    void testCreditTransaction() {
        TransactionSaveCommand command = new TransactionSaveCommand(source.getId(), 0, TEN, CREDIT);
        Transaction transaction = transactionService.create(command);
        transaction = transactionRepository.findById(transaction.getId()).get();
        assertThat(transaction.getState()).isEqualTo(DONE);
        assertThat(transaction.getType()).isEqualTo(CREDIT);
    }

    @Test
    @DisplayName("debit transaction finished without errors")
    void testDebitTransaction() {
        TransactionSaveCommand command = new TransactionSaveCommand(source.getId(), 0, TEN, CREDIT);
        transactionService.create(command);
        command = new TransactionSaveCommand(source.getId(), 0, ONE, DEBIT);

        Transaction transaction = transactionService.create(command);

        transaction = transactionRepository.findById(transaction.getId()).get();
        source = accountRepository.findById(source.getId()).get();

        assertThat(transaction.getState()).isEqualTo(DONE);
        assertThat(transaction.getType()).isEqualTo(DEBIT);
        assertThat(source.getBalance()).isEqualTo(BigDecimal.valueOf(9));
    }
}
