package com.example.psoft_aisafe.airport.domain;

import org.springframework.util.Assert;

/**
 *  Value object representing an Airports's coordinates.
 */

public record Coordinates(double latitude, double longitude) {

    public Coordinates{
        Assert.notNull(latitude, "Latitude must not be null");
        Assert.notNull(longitude, "Longitude must not be null");
    }
}
