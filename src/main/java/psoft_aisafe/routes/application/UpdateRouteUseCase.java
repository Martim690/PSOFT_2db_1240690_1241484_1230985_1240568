package psoft_aisafe.routes.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psoft_aisafe.routes.application.dtos.RouteResponse;
import psoft_aisafe.routes.application.dtos.UpdateRouteRequest;
import psoft_aisafe.routes.domain.Route;
import psoft_aisafe.routes.domain.RouteID;
import psoft_aisafe.routes.domain.RouteRepository;
import psoft_aisafe.routes.domain.RouteRequirements;
import psoft_aisafe.routes.domain.RouteVersion;

@Service
public class UpdateRouteUseCase {

    private final RouteRepository routeRepository;

    public UpdateRouteUseCase(final RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    /**
     * US112 - Update a route. Creates a new RouteVersion preserving history (US111).
     */
    @Transactional
    public RouteResponse update(final String routeId, final UpdateRouteRequest request) {

        final Route route = routeRepository.findByRouteId(RouteID.of(routeId))
                .orElseThrow(() -> new IllegalArgumentException("Route not found: " + routeId));

        if (!route.isActive()) {
            throw new IllegalStateException("Cannot update an inactive route");
        }

        // Use current values if not provided in request
        final RouteVersion current = route.getCurrentVersion()
                .orElseThrow(() -> new IllegalStateException("Route has no current version"));

        final double newDistance = request.getDistance() != null
                ? request.getDistance()
                : current.getDistance();

        final int newFlightTime = request.getEstimatedFlightTime() != null
                ? request.getEstimatedFlightTime()
                : current.getEstimatedFlightTime();

        // Build new requirements (keep existing if not provided)
        RouteRequirements newRequirements = null;
        if (request.getMinimumRange() != null || request.getMinimumCapacity() != null) {
            newRequirements = new RouteRequirements(
                    request.getMinimumRange() != null ? request.getMinimumRange() : route.getRequirements().getMinimumRange(),
                    request.getMinimumCapacity() != null ? request.getMinimumCapacity() : route.getRequirements().getMinimumCapacity(),
                    request.getRequiredCertificationCode() != null ? request.getRequiredCertificationCode() : route.getRequirements().getRequiredCertificationCode()
            );
        }

        route.update(newDistance, newFlightTime, newRequirements, request.getChangeReason());

        return RouteResponse.from(routeRepository.save(route));
    }
}