package com.example.psoft_aisafe.airport.domain;

import org.springframework.util.Assert;

/**
 *  Value object representing an Airports's Runway information.
 */

public record RunwayInfo (String name, int lenght, String orientation){

    public RunwayInfo{
        Assert.hasText(name, "Name must not be empty");
        Assert.hasText(orientation, "Orientation must not be empty");
        Assert.notNull(lenght,  "Lenght must not be null");
        Assert.isTrue(lenght > 0, "Lenght must be greater than 0");
    }
}
