package com.example.psoft_aisafe.airports.domain;

import jakarta.persistence.*;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *  Aggregate root representing an Airport.
 */

@Entity
@Table(name="Airport")
public class Airport {

    // Randomly generated ID for integration with the data base
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "airport_name", nullable = false, unique = true)
    private String name;
    @Column(name = "airport_city", nullable = false)
    private String city;
    @Column(name = "airport_country", nullable = false)
    private String country;
    @Column(name = "airport_region", nullable = false)
    private String region;
    @Column(name = "airport_timezone", nullable = false)
    private String timezone;
    @Column(name = "airport_operationalHours", nullable = false)
    private String operationalHours;
    @Column(name = "airport_photos", nullable = false)
    private List<String> photos;

    @Embedded
    @Column(name = "airport_IATACode", nullable = false, unique = true)
    private IATAcode iataCode;

    @Embedded
    @Column(name = "airport_operationalStatus", nullable = false)
    private AirportStatus operationalStatus;
    @Column(name = "airport_runways", nullable = false)
    private List<RunwayInfo> runways;
    @Embedded
    @Column(name = "airport_facilityInfo", nullable = false)
    private FacilityInfo facilityInfo;

    @Embedded
    private ContactInfo contactInfo;
    @Embedded
    private Coordinates coordinates;

    protected Airport(){} // empty constructor for Entity

    public Airport(
            String name,
            String city,
            String country,
            String region,
            String timezone,
            String operationalHours,
            List<String> photos,
            IATAcode iataCode,
            AirportStatus operationalStatus,
            List<RunwayInfo> runways,
            FacilityInfo facilityInfo,
            ContactInfo contactInfo,
            Coordinates coordinates
    ) {
        Assert.notNull(name, "Airport name must not be null");
        Assert.notNull(city, "Airport city must not be null");
        Assert.notNull(country, "Airport country must not be null");
        Assert.notNull(region, "Airport region must not be null");
        Assert.notNull(timezone, "Airport timezone must not be null");
        Assert.notNull(operationalHours, "Airport operationalHours must not be null");

        this.name = name;
        this.city = city;
        this.country = country;
        this.region = region;
        this.timezone = timezone;
        this.operationalHours = operationalHours;
        this.photos = new ArrayList<String> (photos);
        this.operationalStatus = AirportStatus.OPERATIONAL;
    }

    // Two airports are the same when they have the same IATA Code
    @Override public boolean equals(Object obj){
        if(this == obj) return true;
        if(obj == null) return false;
        if(this.getClass() != obj.getClass()) return false;
        Airport airport = (Airport)obj;
        return Objects.equals(iataCode, airport.iataCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iataCode);
    }
}
