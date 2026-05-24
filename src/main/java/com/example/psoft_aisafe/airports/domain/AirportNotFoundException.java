package com.example.psoft_aisafe.airports.domain;

public class AirportNotFoundException extends DomainException {
    public AirportNotFoundException(IATAcode iatacode) {
        super("Airport not found: " + iatacode);
    }
}
