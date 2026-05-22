package com.example.psoft_aisafe.airport.domain;

import org.springframework.util.Assert;

import java.util.List;

/**
 *  Value object representing an Airports's Facility Information.
 */

public record FacilityInfo (int terminals, int gates, List<String> services){

    public FacilityInfo{
        Assert.notNull(terminals, "Terminals must not be null");
        Assert.notNull(gates, "Gates must not be null");
        Assert.notNull(services, "Services must not be null"); // verificar
    }
}