package psoft_aisafe.routes.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psoft_aisafe.routes.application.dtos.AlternativeRoutesResponse;
import psoft_aisafe.routes.application.dtos.RouteResponse;
import psoft_aisafe.routes.domain.Route;
import psoft_aisafe.routes.domain.RouteRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SearchAlternativeRoutesUseCase {

    private final RouteRepository routeRepository;

    public SearchAlternativeRoutesUseCase(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public AlternativeRoutesResponse execute(String originCode, String destinationCode) {

        List<RouteResponse> directRoutes = routeRepository
                .findByOriginIataCodeAndDestinationIataCodeAndActiveTrue(originCode, destinationCode)
                .stream()
                .map(RouteResponse::from)
                .collect(Collectors.toList());

        List<Route> fromOrigin = routeRepository.findByOriginIataCodeAndActiveTrue(originCode);
        List<Route> toDestination = routeRepository.findByDestinationIataCodeAndActiveTrue(destinationCode);

        Map<String, List<Route>> secondLegs = toDestination.stream()
                .collect(Collectors.groupingBy(Route::getOriginIataCode));

        List<AlternativeRoutesResponse.AlternativeRouteChain> oneStopRoutes = fromOrigin
                .stream()
                .filter(r -> !r.getDestinationIataCode().equals(destinationCode))
                .filter(r -> secondLegs.containsKey(r.getDestinationIataCode()))
                .flatMap(firstLeg ->
                        secondLegs.get(firstLeg.getDestinationIataCode()).stream()
                                .map(secondLeg -> new AlternativeRoutesResponse.AlternativeRouteChain(
                                        RouteResponse.from(firstLeg),
                                        RouteResponse.from(secondLeg),
                                        firstLeg.getDestinationIataCode()
                                ))
                )
                .collect(Collectors.toList());

        return new AlternativeRoutesResponse(originCode, destinationCode, directRoutes, oneStopRoutes);
    }
}