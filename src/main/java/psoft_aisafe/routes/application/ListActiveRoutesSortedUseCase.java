package psoft_aisafe.routes.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psoft_aisafe.routes.application.dtos.RouteWithStatsDTO;
import psoft_aisafe.routes.domain.Route;
import psoft_aisafe.routes.domain.RouteRepository;
import psoft_aisafe.routes.domain.ScheduledFlightRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ListActiveRoutesSortedUseCase {

    private final RouteRepository routeRepository;
    private final ScheduledFlightRepository scheduledFlightRepository;

    public ListActiveRoutesSortedUseCase(RouteRepository routeRepository,
                                         ScheduledFlightRepository scheduledFlightRepository) {
        this.routeRepository           = routeRepository;
        this.scheduledFlightRepository = scheduledFlightRepository;
    }

    /** @param sortBy "popularity" (default) ou "distance" */
    public List<RouteWithStatsDTO> execute(String sortBy) {
        List<RouteWithStatsDTO> result = routeRepository.findByActiveTrue()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        if ("distance".equalsIgnoreCase(sortBy)) {
            result.sort(Comparator.comparingDouble(RouteWithStatsDTO::distanceKm));
        } else {
            result.sort(Comparator.comparingLong(RouteWithStatsDTO::usageCount).reversed());
        }
        return result;
    }

    private RouteWithStatsDTO toDTO(Route route) {
        String routeId = route.getRouteId().getRouteId();
        long usageCount = scheduledFlightRepository.countByRouteId(routeId);

        double distance = route.getCurrentVersion().map(v -> v.getDistance()).orElse(0.0);
        int estimatedFlightTime = route.getCurrentVersion().map(v -> v.getEstimatedFlightTime()).orElse(0);

        return new RouteWithStatsDTO(
                routeId,
                route.getOriginIataCode(),
                route.getDestinationIataCode(),
                estimatedFlightTime,
                route.isActive(),
                route.getRequirements().getMinimumRange(),
                route.getRequirements().getMinimumCapacity(),
                usageCount,
                distance
        );
    }
}