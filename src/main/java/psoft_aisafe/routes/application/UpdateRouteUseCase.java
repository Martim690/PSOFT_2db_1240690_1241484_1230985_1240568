package psoft_aisafe.routes.application;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psoft_aisafe.routes.application.dtos.RouteResponse;
import psoft_aisafe.routes.application.dtos.UpdateRouteRequest;
import psoft_aisafe.routes.domain.Route;
import psoft_aisafe.routes.domain.RouteID;
import psoft_aisafe.routes.domain.RouteRepository;
import psoft_aisafe.routes.domain.RouteRequirements;

@Service
public class UpdateRouteUseCase {

    private final RouteRepository routeRepository;

    public UpdateRouteUseCase(final RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    /**
     * US112 - Update a route (full replacement via PUT). Creates a new RouteVersion preserving history (US111).
     */
    @Transactional
    public RouteResponse update(final String routeId, final UpdateRouteRequest request) {

        final Route route = routeRepository.findByRouteId(RouteID.of(routeId))
                .orElseThrow(() -> new EntityNotFoundException("Route not found: " + routeId));

        if (!route.isActive()) {
            throw new IllegalStateException("Cannot update an inactive route");
        }

        final RouteRequirements newRequirements = new RouteRequirements(
                request.getMinimumRange(),
                request.getMinimumCapacity(),
                request.getRequiredCertificationCode()
        );

        route.update(request.getDistance(), request.getEstimatedFlightTime(), newRequirements, request.getChangeReason());

        return RouteResponse.from(routeRepository.save(route));
    }
}