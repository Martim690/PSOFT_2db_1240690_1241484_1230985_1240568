package com.example.psoft_aisafe.airports.domain;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

@Component
public class AirportFactory {

    /**
     * Creates a fully initialized and valid Airport aggregate.
     */

    public Airport createAirport(
            String code,
            String name,
            String city,
            String country,
            String region,
            String timezone,
            String operationalHours,
            List<String> photos,
            AirportStatus operationalStatus,
            double latitude,
            double longitude,
            List<RunwayInfo> runways,
            FacilityInfo facilityInfo,
            List<ContactInfo> contactInfo
    ) {

        Assert.notNull(code, "IATA code must not be null");
        Assert.isTrue(code.length() == 3, "IATA code must be exactly 3 characters");
        Assert.notEmpty(runways, "An Airport must have at least one (1..*) Runway");
        Assert.notEmpty(contactInfo, "An Airport must have at least one (1..*) Contact");


        IATAcode iataCode = new IATAcode(code);
        Coordinates coordinates = new Coordinates(latitude, longitude);

        Airport airport = new Airport(
                name,
                city,
                country,
                region,
                timezone,
                operationalHours,
                photos,
                iataCode,
                operationalStatus,
                coordinates,
                runways,
                facilityInfo,
                contactInfo
        );

        return airport;
    }
}