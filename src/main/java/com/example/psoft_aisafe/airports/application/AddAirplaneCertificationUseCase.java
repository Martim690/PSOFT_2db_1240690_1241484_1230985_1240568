package com.example.psoft_aisafe.airports.application;

import com.example.psoft_aisafe.UseCase;
import com.example.psoft_aisafe.airports.domain.AirportRepository;
import com.example.psoft_aisafe.airports.domain.IATAcode;
import com.example.psoft_aisafe.airports.infrastructure.AirportService;

import java.util.List;

/**
 *
 */

@UseCase
public class AddAirplaneCertificationUseCase {

    private final AirportRepository airportRepository;

    public AddAirplaneCertificationUseCase(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    public void execute(IATAcode iataCode) {
        /*AirportInformation result = AirportService.search(isbn);
        List<BookAuthor> authorEntities = authors.stream()
                .map(a -> new BookAuthor(a.name(), new ContactEmail(a.contactEmail())))
                .toList();
        airportRepository.save(new Airport(new BookTitle(result.title()), isbn, authorEntities));*/
    }
}
