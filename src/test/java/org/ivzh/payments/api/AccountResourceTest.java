package org.ivzh.payments.api;

import org.ivzh.payments.command.account.AccountSaveCommand;
import org.ivzh.payments.model.account.Account;
import org.ivzh.payments.repository.AccountRepository;
import io.micronaut.context.ApplicationContext;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static io.micronaut.http.HttpRequest.GET;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author ivzh
 */
class AccountResourceTest {

  private RxHttpClient httpClient;
  private AccountSaveCommand command;
  private AccountRepository repository;

  private EmbeddedServer server;

  @BeforeEach
  void setUp() {
    server = ApplicationContext.build().run(EmbeddedServer.class);
    ApplicationContext context = server.getApplicationContext();

    httpClient = context.createBean(RxHttpClient.class, server.getURL());
    repository = context.getBean(AccountRepository.class);

    command = new AccountSaveCommand("1");
  }

  @AfterEach
  void tearDown() {
    server.close();
  }

  @Test
  void testSave() {
    assertThat(repository.findAll()).hasSize(0);

    HttpResponse response = client().exchange(HttpRequest.POST("/api/accounts", command));

    assertThat(response.getStatus().getCode()).isEqualTo(HttpStatus.CREATED.getCode());
    assertThat(repository.findAll()).hasSize(1);
  }

  @Test
  void testById() {
    Account account = repository.save(new Account("1"));

    account = client().retrieve(GET("/api/accounts/" + account.getId()), Account.class);

    assertThat(account).isNotNull();
    assertThat(account.getId()).isGreaterThan(0);
  }

  @Test
  void testFindAll() {
    int totalAccounts = 10;
    insertAccounts(totalAccounts);

    List<Account> accounts = client().retrieve(GET("/api/accounts"), Argument.of(List.class, Account.class));

    assertThat(accounts).isNotEmpty();
    assertThat(accounts).hasSize(totalAccounts);
  }

  private void insertAccounts(int ammount) {
    Stream.iterate(1, n -> n + 1)
        .limit(ammount)
        .forEach(i -> {
          AccountSaveCommand command = new AccountSaveCommand(String.valueOf(i));
          client().exchange(HttpRequest.POST("/api/accounts", command));
        });
  }

  private BlockingHttpClient client() {
    return httpClient.toBlocking();
  }

}