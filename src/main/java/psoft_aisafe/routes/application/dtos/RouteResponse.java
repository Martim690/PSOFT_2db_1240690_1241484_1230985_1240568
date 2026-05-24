package psoft_aisafe.routes.application.dtos;

import org.springframework.hateoas.RepresentationModel;
import psoft_aisafe.routes.domain.Route;
import psoft_aisafe.routes.domain.RouteVersion;

import java.util.List;
import java.util.stream.Collectors;

public class RouteResponse extends RepresentationModel<RouteResponse> {

    private String routeId;
    private String originIataCode;
    private String destinationIataCode;
    private boolean active;

    // Requirements
    private int minimumRange;
    private int minimumCapacity;
    private String requiredCertificationCode;

    // Current version info
    private Double currentDistance;
    private Integer currentEstimatedFlightTime;

    public static RouteResponse from(Route route) {
        RouteResponse dto = new RouteResponse();
        dto.routeId = route.getRouteId().getRouteId();
        dto.originIataCode = route.getOriginIataCode();
        dto.destinationIataCode = route.getDestinationIataCode();
        dto.active = route.isActive();
        dto.minimumRange = route.getRequirements().getMinimumRange();
        dto.minimumCapacity = route.getRequirements().getMinimumCapacity();
        dto.requiredCertificationCode = route.getRequirements().getRequiredCertificationCode();

        route.getCurrentVersion().ifPresent(v -> {
            dto.currentDistance = v.getDistance();
            dto.currentEstimatedFlightTime = v.getEstimatedFlightTime();
        });

        return dto;
    }

    public String getRouteId() { return routeId; }
    public String getOriginIataCode() { return originIataCode; }
    public String getDestinationIataCode() { return destinationIataCode; }
    public boolean isActive() { return active; }
    public int getMinimumRange() { return minimumRange; }
    public int getMinimumCapacity() { return minimumCapacity; }
    public String getRequiredCertificationCode() { return requiredCertificationCode; }
    public Double getCurrentDistance() { return currentDistance; }
    public Integer getCurrentEstimatedFlightTime() { return currentEstimatedFlightTime; }
}