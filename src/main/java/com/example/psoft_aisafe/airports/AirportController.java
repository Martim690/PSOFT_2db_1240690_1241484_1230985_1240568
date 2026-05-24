package com.example.psoft_aisafe.airports;

import com.example.psoft_aisafe.airports.application.DTOs.AirportDTO;
import com.example.psoft_aisafe.airports.domain.Airport;
import com.example.psoft_aisafe.airports.domain.AirportStatus;
import com.example.psoft_aisafe.airports.domain.IATAcode;
import io.swagger.v3.oas.annotations.Operation;
import com.example.psoft_aisafe.airports.application.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 *  REST controller for Airport Aggregate
 *
 *  <p>Exposes five endpoints:</p>
 *  <ul>
 *     <li>{@code POST /airport} — register a new airport</li>
 *     <li>{@code POST /airport/{iataCode}/airplane_certification} — add airplane certification</li>
 *     <li>{@code GET /airport/{iataCode}/} — view airport details</li>
 *     <li>{@code GET /airport} — search airport by city, country or name</li>
 *     <li>{@code PATCH /airport/{iataCode}/status} — update airport status</li>
 *  </ul>
 *
 */

@RestController
@RequestMapping("/airport")
public class AirportController {

    // UseCases WP#2A
    private final RegisterAirportUseCase registerAirport;
    private final AddAirplaneCertificationUseCase addAirplaneCertification;
    private final SearchAirportRepositoryUseCase searchAirportRepository;
    private final UpdateAirportStatusUseCase updateAirportStatus;
    private final ViewAirportDetailsUseCase viewAirportDetails;

    public AirportController(
            RegisterAirportUseCase registerAirport,
            AddAirplaneCertificationUseCase addAirplaneCertification,
            SearchAirportRepositoryUseCase searchAirportRepository,
            UpdateAirportStatusUseCase updateAirportStatus,
            ViewAirportDetailsUseCase viewAirportDetails){
        this.registerAirport = registerAirport;
        this.addAirplaneCertification = addAirplaneCertification;
        this.searchAirportRepository = searchAirportRepository;
        this.updateAirportStatus = updateAirportStatus;
        this.viewAirportDetails = viewAirportDetails;
    }

    // US106
    @PostMapping("/registerAirport")
    @Operation(summary = "Register new airport")
    public ResponseEntity<Airport> registerAirport(
            @RequestParam
            ){
        return ResponseEntity.ok(result);
    }

    // US106a
    @PostMapping("/{iataCode}/airplane_certification")
    @Operation(summary = "Add airplane certification")
    public ResponseEntity<AirportDTO> addAirplaneCertification(
            @PathVariable("iataCode") String iataCode,
            @RequestBody Object certificationDTO) {

        AirportDTO result = addAirplaneCertification.execute(iataCode, certificationDTO);
        return ResponseEntity.ok(result);
    }

    // US107
    @GetMapping("/{iataCode}")
    @Operation(summary = "View airport details")
    public ResponseEntity<AirportDTO> viewAirportDetails(
            @PathVariable("iataCode") String code) {
        AirportDTO result = viewAirportDetails.execute(code);
        return ResponseEntity.ok(result);
    }

    // US108
    @GetMapping("/searchAirport")
    @Operation(summary = "Search airport by city, country or name")
    public ResponseEntity<Airport> searchAirports(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String name) {
        List<AirportDTO> result = searchAirportRepository.execute(city, country, name);
        return ResponseEntity.ok();
    }

    // US109
    @PostMapping("/{iataCode}/status")
    @Operation(summary = "Update Airport Operational Status")
    public ResponseEntity<Airport> updateAirportStatus(
            @PathVariable("iataCode") String code,
            @RequestParam AirportStatus status){
        Airport result = updateAirportStatus.execute(String.valueOf(code), status);
        return ResponseEntity.ok(result);
    }
}
