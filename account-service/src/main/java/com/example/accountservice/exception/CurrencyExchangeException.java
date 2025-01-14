package com.example.accountservice.exception;

public class CurrencyExchangeException extends RuntimeException {
    public CurrencyExchangeException(String message, Throwable cause) {
        super(message, cause);
    }
}
