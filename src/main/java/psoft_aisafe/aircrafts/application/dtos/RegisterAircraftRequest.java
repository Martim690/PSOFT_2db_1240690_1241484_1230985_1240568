package psoft_aisafe.aircrafts.application.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import psoft_aisafe.aircrafts.domain.AircraftStatus;

import java.time.LocalDate;

public record RegisterAircraftRequest(
        @NotBlank(message = "Registration number is required.")
        String registrationNumber,

        @NotBlank(message = "Model name is required.")
        String modelName,

        @NotNull(message = "Manufacturing date is required.")
        @PastOrPresent(message = "Manufacturing date cannot be in the future.")
        LocalDate manufacturingDate,

        @Min(value = 1, message = "Seating capacity must be a positive number.")
        int seatingCapacity,

        @NotNull(message = "Current status is required.")
        AircraftStatus currentStatus
) {}