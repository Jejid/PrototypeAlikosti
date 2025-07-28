package com.example.exception;

public class PayGateTransactionException extends RuntimeException {
    public PayGateTransactionException(String message) {
        super(message);
    }

    public PayGateTransactionException(String message, Throwable cause) {
        super(message, cause);
    }
}