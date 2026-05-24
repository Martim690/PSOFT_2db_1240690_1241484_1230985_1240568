package psoft_aisafe.routes.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psoft_aisafe.routes.domain.Route;
import psoft_aisafe.routes.domain.RouteID;
import psoft_aisafe.routes.domain.RouteRepository;

@Service
public class DeactivateRouteUseCase {

    private final RouteRepository routeRepository;

    public DeactivateRouteUseCase(final RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    /**
     * US112 - Deactivate a route (soft delete — route is kept in database).
     */
    @Transactional
    public void deactivate(final String routeId) {

        final Route route = routeRepository.findByRouteId(RouteID.of(routeId))
                .orElseThrow(() -> new IllegalArgumentException("Route not found: " + routeId));

        if (!route.isActive()) {
            throw new IllegalStateException("Route is already inactive");
        }

        route.deactivate();
        routeRepository.save(route);
    }
}