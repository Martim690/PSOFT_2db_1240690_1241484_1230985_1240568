package psoft_aisafe.aircrafts.application.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import psoft_aisafe.aircrafts.domain.AircraftManufacturer;

public record RegisterAircraftModelRequest(
        @NotNull AircraftManufacturer manufacturer,
        @NotBlank String modelName,
        @Positive int fuelCapacity,
        @Positive int maximumRange,
        @Positive int cruisingSpeed
) {
}