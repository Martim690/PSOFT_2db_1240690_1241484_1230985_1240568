package psoft_aisafe.aircrafts.application.dtos;

public record AircraftOperationalHoursResponse(
        String registrationNumber,
        int totalFlightHours
) {}
