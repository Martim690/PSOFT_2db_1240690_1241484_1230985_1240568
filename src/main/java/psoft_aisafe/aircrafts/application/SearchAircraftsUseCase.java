package psoft_aisafe.aircrafts.application;

import org.springframework.stereotype.Service;
import psoft_aisafe.aircrafts.domain.Aircraft;
import psoft_aisafe.aircrafts.domain.AircraftRepository;
import psoft_aisafe.aircrafts.domain.AircraftStatus;

import java.util.List;

@Service
public class SearchAircraftsUseCase {

    private final AircraftRepository aircraftRepository;

    public SearchAircraftsUseCase(AircraftRepository aircraftRepository) {
        this.aircraftRepository = aircraftRepository;
    }

    public List<Aircraft> execute(String modelName, AircraftStatus status, Integer year) {
        return aircraftRepository.searchAircrafts(modelName, status, year);
    }
}