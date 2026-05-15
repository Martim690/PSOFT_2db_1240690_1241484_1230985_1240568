package com.example.psoft_aisafe.model.classes;

import org.springframework.util.Assert;

/**
 *  Value object representing an Airports's IATA Code.
 */

public record IATAcode (String code){

    public IATAcode {
        Assert.notNull(code, "code must not be null");
    }
}
