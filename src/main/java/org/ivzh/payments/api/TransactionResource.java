package org.ivzh.payments.api;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.validation.Validated;
import io.reactivex.Single;
import org.ivzh.payments.api.dto.TransactionDto;
import org.ivzh.payments.command.transaction.TransactionSaveCommand;
import org.ivzh.payments.model.transaction.Transaction;
import org.ivzh.payments.service.TransactionService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

/**
 * @author ivzh
 */
@Validated
@Controller("/api/transactions")
public class TransactionResource {

  private final TransactionService service;

  public TransactionResource(TransactionService service) {
    this.service = service;
  }

  @Post
  public Single<HttpResponse<TransactionDto>> create(@Valid @Body Single<TransactionSaveCommand> command) {
    return command.map(c -> {
      Transaction transaction = service.create(c);
      return HttpResponse
          .created(new TransactionDto(transaction))
          .headers(headers -> headers.location(location(transaction)));
    });
  }

  @Get("/{id}")
  public Single<TransactionDto> byId(@NotNull long id) {
    return Single.just(service.findById(id));
  }

  @Get("/account/{id}")
  public Single<List<TransactionDto>> fromAccount(@NotNull long id) {
    return Single.just(service.fromAccount(id));
  }

  private URI location(Transaction transaction) {
    return URI.create(String.format("/api/transactions/%s", transaction.getId()));
  }
}
