package psoft_aisafe.routes.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psoft_aisafe.routes.application.dtos.CreateRouteRequest;
import psoft_aisafe.routes.application.dtos.RouteResponse;
import psoft_aisafe.routes.domain.Route;
import psoft_aisafe.routes.domain.RouteRepository;
import psoft_aisafe.routes.domain.RouteRequirements;

@Service
public class CreateRouteUseCase {

    private final RouteRepository routeRepository;

    public CreateRouteUseCase(final RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    @Transactional
    public RouteResponse create(final CreateRouteRequest request) {

        final String origin = request.getOriginIataCode();
        final String destination = request.getDestinationIataCode();

        if (routeRepository.existsActiveRouteBetween(origin, destination)) {
            throw new IllegalStateException(
                    "An active route already exists between " + origin + " and " + destination
            );
        }

        final RouteRequirements requirements = new RouteRequirements(
                request.getMinimumRange(),
                request.getMinimumCapacity(),
                request.getRequiredCertificationCode()
        );

        final Route route = new Route(
                origin,
                destination,
                requirements,
                request.getDistance(),
                request.getEstimatedFlightTime()
        );

        return RouteResponse.from(routeRepository.save(route));
    }
}