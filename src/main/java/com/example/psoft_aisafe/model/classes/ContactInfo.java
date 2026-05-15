package com.example.psoft_aisafe.model.classes;

import com.example.psoft_aisafe.model.enums.ContactType;
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
