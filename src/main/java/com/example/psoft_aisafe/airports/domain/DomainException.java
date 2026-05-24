package com.example.psoft_aisafe.airports.domain;

public abstract class DomainException extends RuntimeException {
    protected DomainException(String message) {
        super(message);
    }
}