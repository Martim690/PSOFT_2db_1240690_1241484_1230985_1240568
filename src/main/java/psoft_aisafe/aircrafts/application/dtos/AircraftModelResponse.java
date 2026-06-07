package psoft_aisafe.aircrafts.application.dtos;

public record AircraftModelResponse(
        String modelName,
        String manufacturer,
        int fuelCapacity,
        int maximumRange,
        int cruisingSpeed
) {}