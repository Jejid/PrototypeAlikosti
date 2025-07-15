package com.example.exception;

public class PayuTransactionException extends RuntimeException {
    public PayuTransactionException(String message) {
        super(message);
    }

    public PayuTransactionException(String message, Throwable cause) {
        super(message, cause);
    }
}