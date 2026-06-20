package psoft_aisafe.aircrafts.application;

import org.springframework.stereotype.Service;
import psoft_aisafe.aircrafts.application.dtos.FleetStatusResponse;
import psoft_aisafe.aircrafts.domain.AircraftRepository;
import psoft_aisafe.aircrafts.domain.Aircraft;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GetFleetStatusUseCase {

    private final AircraftRepository aircraftRepository;

    public GetFleetStatusUseCase(AircraftRepository aircraftRepository) {
        this.aircraftRepository = aircraftRepository;
    }

    public FleetStatusResponse execute() {
        List<Aircraft> allAircrafts = aircraftRepository.findAll();

        Map<String, Long> counts = allAircrafts.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getCurrentStatus().name(),
                        Collectors.counting()
                ));

        List<FleetStatusResponse.AircraftStatusDetail> details = allAircrafts.stream()
                .map(a -> new FleetStatusResponse.AircraftStatusDetail(
                        a.getRegistrationNumber().getNumber(),
                        a.getCurrentStatus().name()
                ))
                .toList();

        return new FleetStatusResponse(counts, details);
    }
}