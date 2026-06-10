package psoft_aisafe.aircrafts.application;

import org.springframework.stereotype.Service;
import psoft_aisafe.aircrafts.application.dtos.AircraftResponse;
import psoft_aisafe.aircrafts.domain.AircraftRepository;

import java.util.List;

@Service
public class ListAircraftsUseCase {

    private final AircraftRepository aircraftRepository;

    public ListAircraftsUseCase(AircraftRepository aircraftRepository) {
        this.aircraftRepository = aircraftRepository;
    }

    public List<AircraftResponse> execute() {
        return aircraftRepository.findAll()
                .stream()
                .map(aircraft -> new AircraftResponse(
                        aircraft.getRegistrationNumber().getNumber(),
                        aircraft.getModel().getModelName(),
                        aircraft.getManufacturingDate(),
                        aircraft.getSeatingCapacity(),
                        aircraft.getCurrentStatus().name()
                ))
                .toList();
    }
}