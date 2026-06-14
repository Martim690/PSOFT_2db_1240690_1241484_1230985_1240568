package psoft_aisafe.aircrafts.application.dtos;

import java.time.LocalDate;

public record AircraftResponse(
        String registrationNumber,
        String model,
        LocalDate manufacturingDate,
        Integer seatingCapacity,
        String currentStatus
) {}