package psoft_aisafe.aircrafts.application.dtos;

import java.time.LocalDate;

public record AircraftResponse(
        String registrationNumber,
        String model, // Tem de ser 'model' para o Postman não dar erro!
        LocalDate manufacturingDate,
        Integer seatingCapacity,
        String currentStatus
) {}