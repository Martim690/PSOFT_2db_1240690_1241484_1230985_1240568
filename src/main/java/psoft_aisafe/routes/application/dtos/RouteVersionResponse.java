package psoft_aisafe.routes.application.dtos;

import psoft_aisafe.routes.domain.RouteVersion;

import java.time.LocalDateTime;

public class RouteVersionResponse {

    private Long id;
    private double distance;
    private int estimatedFlightTime;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
    private String changeReason;
    private boolean current;

    public static RouteVersionResponse from(RouteVersion version) {
        RouteVersionResponse dto = new RouteVersionResponse();
        dto.id = version.getId();
        dto.distance = version.getDistance();
        dto.estimatedFlightTime = version.getEstimatedFlightTime();
        dto.validFrom = version.getValidFrom();
        dto.validUntil = version.getValidUntil();
        dto.changeReason = version.getChangeReason();
        dto.current = version.isCurrent();
        return dto;
    }

    public Long getId() { return id; }
    public double getDistance() { return distance; }
    public int getEstimatedFlightTime() { return estimatedFlightTime; }
    public LocalDateTime getValidFrom() { return validFrom; }
    public LocalDateTime getValidUntil() { return validUntil; }
    public String getChangeReason() { return changeReason; }
    public boolean isCurrent() { return current; }
}