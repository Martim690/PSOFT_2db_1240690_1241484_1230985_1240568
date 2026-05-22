package com.example.psoft_aisafe.airport.domain;

import org.springframework.util.Assert;

/**
 *  Value object representing an Airport's contact information
 */

public record ContactInfo (ContactType contactType, String value, String department){

    public ContactInfo{
        Assert.isInstanceOf(ContactType.class, contactType);
        Assert.hasText(value, "Value must not be empty");
        Assert.hasText(department, "Department must not be empty");
    }
}
