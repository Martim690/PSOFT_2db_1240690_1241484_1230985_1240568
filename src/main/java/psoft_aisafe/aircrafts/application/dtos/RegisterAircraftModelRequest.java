package psoft_aisafe.aircrafts.application.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import psoft_aisafe.aircrafts.domain.AircraftManufacturer;
public record RegisterAircraftModelRequest(
        @NotNull(message = "Manufacturer is required.")
        AircraftManufacturer manufacturer,

        @NotBlank(message = "Model name is required.")
        String modelName,

        @Min(value = 1, message = "Fuel capacity must be a positive number.")
        int fuelCapacity,

        @Min(value = 1, message = "Maximum range must be a positive number.")
        int maximumRange,

        @Min(value = 1, message = "Cruising speed must be a positive number.")
        int cruisingSpeed
) {}
