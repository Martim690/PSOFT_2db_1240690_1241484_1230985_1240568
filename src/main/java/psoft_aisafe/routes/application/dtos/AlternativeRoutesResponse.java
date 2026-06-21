package psoft_aisafe.routes.application.dtos;
import java.util.List;

/** Usado pela US216 */
public record AlternativeRoutesResponse(
        String originAirport,
        String destinationAirport,
        List<RouteResponse> directRoutes,
        List<AlternativeRouteChain> oneStopRoutes
) {
    public record AlternativeRouteChain(
            RouteResponse firstLeg,
            RouteResponse secondLeg,
            String connectionAirport
    ) {}
}