package com.foodey.server.exceptions;

/** DatabaseErrorException */
public class DatabaseErrorException extends Exception {

  public DatabaseErrorException() {}

  public DatabaseErrorException(String message) {
    super(message);
  }

  public DatabaseErrorException(String message, Throwable cause) {
    super(message, cause);
  }

  public DatabaseErrorException(Throwable cause) {
    super(cause);
  }

  protected DatabaseErrorException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
