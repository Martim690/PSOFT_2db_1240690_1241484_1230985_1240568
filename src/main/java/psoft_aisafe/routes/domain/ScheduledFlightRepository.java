package psoft_aisafe.routes.domain;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduledFlightRepository {

    ScheduledFlight save(ScheduledFlight scheduledFlight);

    List<ScheduledFlight> findByAircraftRegistration(String registration);

    long countByRouteId(String routeId);

    List<ScheduledFlight> findByAircraftRegistrationAndScheduledDateTimeBetween(
            String registration, LocalDateTime start, LocalDateTime end);
}