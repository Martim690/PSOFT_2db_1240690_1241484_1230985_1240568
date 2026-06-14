package psoft_aisafe.aircrafts.application;

import org.springframework.stereotype.Service;
import psoft_aisafe.aircrafts.application.dtos.CompatibleRouteResponse;
import psoft_aisafe.aircrafts.domain.Aircraft;
import psoft_aisafe.aircrafts.domain.AircraftRepository;
import psoft_aisafe.aircrafts.domain.RegistrationNumber;
import psoft_aisafe.routes.domain.Route;
import psoft_aisafe.routes.domain.RouteRepository;

import java.util.List;

@Service
public class GetCompatibleRoutesUseCase {

    private final AircraftRepository aircraftRepository;
    private final RouteRepository routeRepository;

    public GetCompatibleRoutesUseCase(AircraftRepository aircraftRepository, RouteRepository routeRepository) {
        this.aircraftRepository = aircraftRepository;
        this.routeRepository = routeRepository;
    }

    public List<CompatibleRouteResponse> execute(String registrationText) {
        RegistrationNumber registrationNumber = new RegistrationNumber(registrationText);

        // 1. Procurar a aeronave pretendida
        Aircraft aircraft = aircraftRepository.findByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new IllegalArgumentException("Aircraft not found with registration: " + registrationText));

        int aircraftRange = aircraft.getModel().getMaximumRange();
        int aircraftCapacity = aircraft.getSeatingCapacity(); // Lotação específica da instância

        // 2. Procurar todas as rotas e filtrar apenas as compatíveis e ativas
        return routeRepository.findAll().stream()
                .filter(Route::isActive)
                .filter(route -> aircraftRange >= route.getRequirements().getMinimumRange())
                .filter(route -> aircraftCapacity >= route.getRequirements().getMinimumCapacity())
                .map(route -> new CompatibleRouteResponse(
                        route.getRouteId().toString(),
                        route.getOriginIataCode(),
                        route.getDestinationIataCode(),
                        route.getRequirements().getMinimumRange(),
                        route.getRequirements().getMinimumCapacity(),
                        route.isActive()
                ))
                .toList();
    }
}