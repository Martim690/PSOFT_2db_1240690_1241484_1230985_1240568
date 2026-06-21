package psoft_aisafe.routes.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psoft_aisafe.routes.application.dtos.ScheduledFlightResponse;
import psoft_aisafe.routes.domain.ScheduledFlightRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class GetScheduledFlightsByAircraftUseCase {

    private final ScheduledFlightRepository scheduledFlightRepository;

    public GetScheduledFlightsByAircraftUseCase(ScheduledFlightRepository repo) {
        this.scheduledFlightRepository = repo;
    }

    public List<ScheduledFlightResponse> execute(String aircraftRegistration) {
        return scheduledFlightRepository
                .findByAircraftRegistration(aircraftRegistration)
                .stream()
                .map(ScheduledFlightResponse::from)
                .collect(Collectors.toList());
    }
}