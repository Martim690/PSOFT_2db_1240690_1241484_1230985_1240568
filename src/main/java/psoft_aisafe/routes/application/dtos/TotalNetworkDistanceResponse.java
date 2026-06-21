package psoft_aisafe.routes.application.dtos;

/** Usado pela US215 */
public record TotalNetworkDistanceResponse(
        double totalDistanceKm,
        int activeRouteCount
) {}
