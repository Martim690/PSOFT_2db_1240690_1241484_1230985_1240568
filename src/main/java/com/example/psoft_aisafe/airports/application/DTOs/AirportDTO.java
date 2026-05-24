package com.example.psoft_aisafe.airports.application.DTOs;

import com.example.psoft_aisafe.airports.domain.*;

import java.util.List;

/**
 * Data Transfer Object for Airport Class
 */

public class AirportDTO {
    public String name;
    public String city;
    public String country;
    public String region;
    public String timezone;
    public String operationalHours;
    public List<String> photos;
    public AirportStatus operationalStatus;

    public IATAcode iataCode;
    public Coordinates coordinates;
    public List<RunwayInfo> runways;
    public FacilityInfo facilityInfo;
    public ContactInfo contactInfo;
}
