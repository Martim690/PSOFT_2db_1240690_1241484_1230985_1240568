package com.example.psoft_aisafe.airports.application;

import com.example.psoft_aisafe.airports.application.DTOs.AirportDTO;
import com.example.psoft_aisafe.airports.domain.AirportRepository;

import java.util.List;

/**
 * Use Case: search for airports by city, country, or name
 */

public class SearchAirportRepositoryUseCase {

    private final AirportRepository airportRepository;

    public SearchAirportRepositoryUseCase(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    public List<AirportDTO> execute(String city, String country, String name) {

        return null;
    }
}
