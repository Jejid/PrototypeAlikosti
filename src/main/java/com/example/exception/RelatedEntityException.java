package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) //
public class RelatedEntityException extends RuntimeException {
    public RelatedEntityException(String message) {
        super(message);
    }
}