package org.ivzh.payments.api;

import io.micronaut.context.ApplicationContext;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import org.ivzh.payments.api.handler.Error;
import org.ivzh.payments.model.account.Account;
import org.ivzh.payments.model.transaction.Transaction;
import org.ivzh.payments.model.transaction.TransactionBuilder;
import org.ivzh.payments.repository.AccountRepository;
import org.ivzh.payments.repository.TransactionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.micronaut.http.HttpRequest.GET;
import static java.math.BigDecimal.TEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.ivzh.payments.model.transaction.TransactionType.CREDIT;

/**
 * @author ivzh
 */
class TransactionResourceTest {

  private RxHttpClient httpClient;
  private AccountRepository accountRepository;
  private TransactionRepository transactionRepository;

  private Account source;
  private EmbeddedServer server;
  private Transaction transaction;

  @BeforeEach
  void setUp() {
    server = ApplicationContext.build().run(EmbeddedServer.class);
    ApplicationContext context = server.getApplicationContext();

    httpClient = context.createBean(RxHttpClient.class, server.getURL());
    transactionRepository = context.getBean(TransactionRepository.class);
    accountRepository = context.getBean(AccountRepository.class);

    source = accountRepository.save(new Account("1"));
    transaction = TransactionBuilder.create()
        .source(source)
        .ammount(TEN)
        .type(CREDIT)
        .build();
    transaction = transactionRepository.save(transaction);
  }

  @AfterEach
  void tearDown() {
    server.close();
  }


  @Test
  void testTransactionById_notFound() {
    assertThatThrownBy(() -> client().retrieve(GET("/api/transactions/777"), Error.class))
        .isInstanceOf(HttpClientResponseException.class)
        .hasMessage("Transaction 777 not found");
  }

  @Test
  void testTransactionById_transactionFound() {
    long id = transaction.getId();
    String response = client().retrieve(GET("/api/transactions/" + id));
    assertThat(response).isNotNull();
  }

  @Test
  void testFromAccount() {
    long sourceId = source.getId();
    String response = client().retrieve(GET("/api/transactions/account/" + sourceId));
    assertThat(response).isNotNull();
  }

  private BlockingHttpClient client() {
    return httpClient.toBlocking();
  }

}