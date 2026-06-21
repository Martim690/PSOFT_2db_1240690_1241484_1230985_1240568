package psoft_aisafe.routes.application.dtos;

public record RouteWithStatsDTO(
        String routeId,
        String originIataCode,
        String destinationIataCode,
        int estimatedFlightTimeMinutes,
        boolean active,
        int minimumRange,
        int minimumCapacity,
        long usageCount,
        double distanceKm
) {}