package psoft_aisafe.routes.application;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psoft_aisafe.routes.application.dtos.RouteResponse;
import psoft_aisafe.routes.application.dtos.RouteVersionResponse;
import psoft_aisafe.routes.domain.RouteID;
import psoft_aisafe.routes.domain.RouteRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetRouteByIdUseCase {

    private final RouteRepository routeRepository;

    public GetRouteByIdUseCase(final RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    /**
     * US113 - View the details of a route given its ID.
     */
    @Transactional(readOnly = true)
    public RouteResponse getById(final String routeId) {
        return routeRepository.findByRouteId(RouteID.of(routeId))
                .map(RouteResponse::from)
                .orElseThrow(() -> new EntityNotFoundException("Route not found: " + routeId));
    }

    /**
     * US111 - View the full version history of a route.
     */
    @Transactional(readOnly = true)
    public List<RouteVersionResponse> getHistory(final String routeId) {
        return routeRepository.findByRouteId(RouteID.of(routeId))
                .map(route -> route.getVersions().stream()
                        .map(RouteVersionResponse::from)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new EntityNotFoundException("Route not found: " + routeId));
    }
}