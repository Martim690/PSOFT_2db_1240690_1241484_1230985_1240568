package psoft_aisafe.routes.application.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class UpdateRouteRequest {

    @Min(value = 1, message = "Estimated flight time must be at least 1 minute")
    private Integer estimatedFlightTime;

    @Min(value = 1, message = "Distance must be positive")
    private Double distance;

    @Min(value = 1, message = "Minimum range must be positive")
    private Integer minimumRange;

    @Min(value = 1, message = "Minimum capacity must be positive")
    private Integer minimumCapacity;

    private String requiredCertificationCode;

    @NotBlank(message = "Change reason is required when updating a route")
    private String changeReason;

    // Getters and setters

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

    public String getChangeReason() { return changeReason; }
    public void setChangeReason(String changeReason) { this.changeReason = changeReason; }
}