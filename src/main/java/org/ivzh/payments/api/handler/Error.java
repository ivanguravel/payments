package org.ivzh.payments.api.handler;


/**
 * @author ivzh
 */
public class Error {

  private final int status;
  private final String message;

  public Error(int status, String message) {
    this.message = message;
    this.status = status;
  }

  public int getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }
}
