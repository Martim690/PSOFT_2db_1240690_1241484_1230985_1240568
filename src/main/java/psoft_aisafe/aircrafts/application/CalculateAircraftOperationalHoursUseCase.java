package psoft_aisafe.aircrafts.application;

import org.springframework.stereotype.Service;
import psoft_aisafe.aircrafts.application.dtos.AircraftOperationalHoursResponse;
import psoft_aisafe.aircrafts.domain.AircraftRepository;

import java.util.List;

@Service
public class CalculateAircraftOperationalHoursUseCase {

    private final AircraftRepository aircraftRepository;

    public CalculateAircraftOperationalHoursUseCase(AircraftRepository aircraftRepository) {
        this.aircraftRepository = aircraftRepository;
    }

    public List<AircraftOperationalHoursResponse> execute() {
        return aircraftRepository.findAll().stream()
                .map(aircraft -> new AircraftOperationalHoursResponse(
                        // Dependendo de como a tua classe RegistrationNumber está feita,
                        // podes usar .toString() ou o getter direto se tiveres um (ex: .getRegistrationNumber())
                        aircraft.getRegistrationNumber().getNumber(),
                        aircraft.getTotalFlightHours()
                ))
                .toList();
    }
}