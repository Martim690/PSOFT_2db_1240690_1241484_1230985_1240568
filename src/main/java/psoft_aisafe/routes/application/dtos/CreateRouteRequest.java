package psoft_aisafe.routes.application.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class CreateRouteRequest {

    @NotBlank(message = "Origin IATA code is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Origin IATA code must be exactly 3 uppercase letters")
    private String originIataCode;

    @NotBlank(message = "Destination IATA code is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Destination IATA code must be exactly 3 uppercase letters")
    private String destinationIataCode;

    @NotNull(message = "Estimated flight time is required")
    @Min(value = 1, message = "Estimated flight time must be at least 1 minute")
    private Integer estimatedFlightTime; // in minutes

    @NotNull(message = "Distance is required")
    @Min(value = 1, message = "Distance must be positive")
    private Double distance; // in km

    @NotNull(message = "Minimum range is required")
    @Min(value = 1, message = "Minimum range must be positive")
    private Integer minimumRange; // in km

    @NotNull(message = "Minimum capacity is required")
    @Min(value = 1, message = "Minimum capacity must be positive")
    private Integer minimumCapacity;

    private String requiredCertificationCode;

    // Getters and setters

    public String getOriginIataCode() { return originIataCode; }
    public void setOriginIataCode(String originIataCode) { this.originIataCode = originIataCode; }

    public String getDestinationIataCode() { return destinationIataCode; }
    public void setDestinationIataCode(String destinationIataCode) { this.destinationIataCode = destinationIataCode; }

    public Integer getEstimatedFlightTime() { return estimatedFlightTime; }
    public void setEstimatedFlightTime(Integer estimatedFlightTime) { this.estimatedFlightTime = estimatedFlightTime; }

    public Double getDistance() { return distance; }
    public void setDistance(Double distance) { this.distance = distance; }

    public Integer getMinimumRange() { return minimumRange; }
    public void setMinimumRange(Integer minimumRange) { this.minimumRange = minimumRange; }

    public Integer getMinimumCapacity() { return minimumCapacity; }
    public void setMinimumCapacity(Integer minimumCapacity) { this.minimumCapacity = minimumCapacity; }

    public String getRequiredCertificationCode() { return requiredCertificationCode; }
    public void setRequiredCertificationCode(String requiredCertificationCode) { this.requiredCertificationCode = requiredCertificationCode; }
}