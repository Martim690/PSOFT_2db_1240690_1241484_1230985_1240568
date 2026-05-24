package com.example.psoft_aisafe.airports.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link Airport} entities.
 */

public interface AirportRepository extends CrudRepository<Airport, Long> {

    List<Airport> findByIATAcode(IATAcode iatacode);
}