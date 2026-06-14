package psoft_aisafe.aircrafts.application.dtos;

import jakarta.validation.constraints.Min;

public record UpdateAircraftModelSpecsRequest(
        @Min(value = 1, message = "Fuel capacity must be a positive number.")
        Integer fuelCapacity,

        @Min(value = 1, message = "Maximum range must be a positive number.")
        Integer maximumRange,

        @Min(value = 1, message = "Cruising speed must be a positive number.")
        Integer cruisingSpeed
) {}