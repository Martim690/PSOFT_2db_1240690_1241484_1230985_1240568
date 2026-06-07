package psoft_aisafe.aircrafts.application.dtos;

import java.time.LocalDate;

public record AircraftResponse(
        String registrationNumber,
        String modelName,
        LocalDate manufacturingDate,
        int seatingCapacity,
        String currentStatus
) {}