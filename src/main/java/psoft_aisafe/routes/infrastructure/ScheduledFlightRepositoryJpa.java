package psoft_aisafe.routes.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import psoft_aisafe.routes.domain.ScheduledFlight;
import psoft_aisafe.routes.domain.ScheduledFlightRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduledFlightRepositoryJpa
        extends JpaRepository<ScheduledFlight, Long>, ScheduledFlightRepository {

    @Override
    List<ScheduledFlight> findByAircraftRegistration(String registration);

    @Override
    long countByRouteId(String routeId);

    @Override
    List<ScheduledFlight> findByAircraftRegistrationAndScheduledDateTimeBetween(
            String registration, LocalDateTime start, LocalDateTime end);
}