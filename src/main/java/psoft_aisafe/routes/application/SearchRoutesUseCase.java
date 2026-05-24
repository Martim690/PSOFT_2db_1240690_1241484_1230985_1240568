package psoft_aisafe.routes.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psoft_aisafe.routes.application.dtos.RouteResponse;
import psoft_aisafe.routes.domain.RouteRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchRoutesUseCase {

    private final RouteRepository routeRepository;

    public SearchRoutesUseCase(final RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    /**
     * US114 - Search for routes by origin, destination, or both.
     * Both parameters are optional — if neither is provided, returns all routes.
     */
    @Transactional(readOnly = true)
    public List<RouteResponse> search(final String originCode, final String destinationCode) {

        final String origin = (originCode != null && !originCode.isBlank())
                ? (originCode) : null;

        final String destination = (destinationCode != null && !destinationCode.isBlank())
                ? (destinationCode) : null;

        return routeRepository.search(origin, destination)
                .stream()
                .map(RouteResponse::from)
                .collect(Collectors.toList());
    }
}