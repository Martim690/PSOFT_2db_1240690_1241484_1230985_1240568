package psoft_aisafe.aircrafts.application.dtos;

import jakarta.validation.constraints.NotNull;
import psoft_aisafe.aircrafts.domain.AircraftStatus;

public record UpdateAircraftStatusRequest(
        @NotNull(message = "New status is required.")
        AircraftStatus status
) {}