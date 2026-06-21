package psoft_aisafe.routes.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psoft_aisafe.routes.application.dtos.TotalNetworkDistanceResponse;
import psoft_aisafe.routes.domain.Route;
import psoft_aisafe.routes.domain.RouteRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GetTotalNetworkDistanceUseCase {

    private final RouteRepository routeRepository;

    public GetTotalNetworkDistanceUseCase(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public TotalNetworkDistanceResponse execute() {
        List<Route> active = routeRepository.findByActiveTrue();

        double total = active.stream()
                .mapToDouble(r -> r.getCurrentVersion().map(v -> v.getDistance()).orElse(0.0))
                .sum();

        return new TotalNetworkDistanceResponse(total, active.size());
    }
}