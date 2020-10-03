package org.ivzh.payments.api;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.validation.Validated;
import io.reactivex.Single;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.ivzh.payments.command.account.AccountSaveCommand;
import org.ivzh.payments.model.account.Account;
import org.ivzh.payments.service.AccountService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

/**
 * @author ivzh
 */
@Validated
@Tag(name = "Accounts")
@Controller("/api/accounts")
public class AccountResource {

  private final AccountService service;

  public AccountResource(AccountService service) {
    this.service = service;
  }

  @Get(produces = MediaType.APPLICATION_JSON)
  public Single<List<Account>> findAll() {
    return Single.just(service.findAll());
  }

  @Get("/{id}")
  public Single<Account> byId(@NotNull long id) {
    return Single.just(service.findById(id));
  }

  @Post(consumes = MediaType.APPLICATION_JSON)
  public Single<HttpResponse<Account>> create(@Valid @Body Single<AccountSaveCommand> command) {
    return command.map(c -> {
      Account account = service.create(c);
      return HttpResponse
          .created(account)
          .headers(headers -> headers.location(location(account)));
    });
  }

  private URI location(Account account) {
    return URI.create(String.format("/api/accounts/%s", account.getId()));
  }

}
