package org.ivzh.payments.service;

import org.ivzh.payments.command.account.AccountSaveCommand;
import org.ivzh.payments.model.account.Account;
import org.ivzh.payments.repository.AccountRepository;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import java.util.List;

/**
 * @author ivzh
 */
@Singleton
public class AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    @Inject
    private AccountRepository accountRepository;

    public Account create(@Valid AccountSaveCommand command) {
        LOGGER.debug("Inserting account {}", command);
        Validate.notNull(command, "Invalid account data");
        Account created = accountRepository.save(Account.from(command));
        LOGGER.info("Account created: {}", created);
        return created;
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account findById(long id) {
        return accountRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Account does not exist"));
    }

}
