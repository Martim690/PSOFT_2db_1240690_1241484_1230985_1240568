package com.example.psoft_aisafe.airports.domain;

import org.springframework.util.Assert;

/**
 *  Value object representing an Airports's IATA Code.
 *
 *  Is a unique identifier of each Airport
 */

public record IATAcode (String code){

    public IATAcode {
        Assert.hasText(code, "code must have text");
        assert code.length() == 3 : "Code has to have only 3 letters";
    }
}
