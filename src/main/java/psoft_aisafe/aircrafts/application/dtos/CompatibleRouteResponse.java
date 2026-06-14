package psoft_aisafe.aircrafts.application.dtos;

public record CompatibleRouteResponse(
        String routeId,
        String originIataCode,
        String destinationIataCode,
        int minimumRange,
        int minimumCapacity,
        boolean isActive
) {}