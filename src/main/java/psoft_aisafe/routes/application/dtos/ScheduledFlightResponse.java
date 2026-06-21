package psoft_aisafe.routes.application.dtos;

import psoft_aisafe.routes.domain.ScheduledFlight;
import psoft_aisafe.routes.domain.ScheduledFlightStatus;
import java.time.LocalDateTime;

public record ScheduledFlightResponse(
        Long id,
        String routeId,
        String aircraftRegistration,
        LocalDateTime scheduledDateTime,
        ScheduledFlightStatus status
) {
    public static ScheduledFlightResponse from(ScheduledFlight sf) {
        return new ScheduledFlightResponse(
                sf.getId(),
                sf.getRouteId(),
                sf.getAircraftRegistration(),
                sf.getScheduledDateTime(),
                sf.getStatus()
        );
    }
}