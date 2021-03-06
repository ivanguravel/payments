package org.ivzh.payments.api.handler;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

/**
 * @author ivzh
 */
@Singleton
@Produces
@Requires(classes = {RuntimeException.class, ExceptionHandler.class})
public class GeneralExceptionHandler implements ExceptionHandler<RuntimeException, HttpResponse> {

  private static final Logger LOGGER = LoggerFactory.getLogger(GeneralExceptionHandler.class);

  @Override
  public HttpResponse handle(HttpRequest request, RuntimeException exception) {
    LOGGER.error("Error occured", exception);
    Error error = new Error(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), exception.getMessage());
    return HttpResponse.serverError(error);
  }

}
