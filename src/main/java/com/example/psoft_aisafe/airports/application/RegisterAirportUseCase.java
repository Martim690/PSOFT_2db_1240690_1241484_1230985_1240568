package com.example.psoft_aisafe.airports.application;

import com.example.psoft_aisafe.airports.domain.Airport;
import com.example.psoft_aisafe.airports.domain.AirportRepository;
import com.example.psoft_aisafe.airports.domain.IATAcode;
import com.example.psoft_aisafe.airports.infrastructure.AirportService;
import org.springframework.stereotype.Service;

/**
 *
 */

@Service
public class RegisterAirportUseCase {

    private final AirportRepository airportRepository;
    private AirportService airportService;

    public RegisterAirportUseCase(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
        this.airportService = airportService;
    }

    public Airport execute(){
        String iataCode;
        IATAcode code = new IATAcode(iataCode);

        return AirportRepository.save();
    }
}
