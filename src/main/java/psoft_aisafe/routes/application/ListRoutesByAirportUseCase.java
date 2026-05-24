package psoft_aisafe.routes.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psoft_aisafe.model.classes.IATAcode;
import psoft_aisafe.routes.application.dtos.RouteResponse;
import psoft_aisafe.routes.domain.RouteRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListRoutesByAirportUseCase {

    private final RouteRepository routeRepository;

    public ListRoutesByAirportUseCase(final RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    /**
     * US113 - View all routes from/to a specific airport.
     */
    @Transactional(readOnly = true)
    public List<RouteResponse> listByAirport(final String iataCode) {
        return routeRepository.findByOriginOrDestination(iataCode)
                .stream()
                .map(RouteResponse::from)
                .collect(Collectors.toList());
    }
}