package psoft_aisafe.aircrafts.application;

import org.springframework.stereotype.Service;
import psoft_aisafe.aircrafts.application.dtos.FleetStatusResponse;
import psoft_aisafe.aircrafts.domain.Aircraft;
import psoft_aisafe.aircrafts.domain.AircraftRepository;
import psoft_aisafe.aircrafts.domain.AircraftStatus;

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
        List<Aircraft> fleet = aircraftRepository.findAll();
        long total = fleet.size();

        Map<String, Long> statusCounts = fleet.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getCurrentStatus().name(),
                        Collectors.counting()
                ));

        for (AircraftStatus status : AircraftStatus.values()) {
            statusCounts.putIfAbsent(status.name(), 0L);
        }

        return new FleetStatusResponse(total, statusCounts);
    }
}
