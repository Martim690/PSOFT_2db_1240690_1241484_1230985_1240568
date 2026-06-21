package psoft_aisafe.routes.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import psoft_aisafe.routes.application.dtos.RouteResponse;
import psoft_aisafe.routes.domain.Route;
import psoft_aisafe.routes.domain.RouteRepository;
import psoft_aisafe.routes.domain.RouteRequirements;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListRoutesByAirportUseCaseTest {

    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private ListRoutesByAirportUseCase useCase;

    private Route createRoute(String origin, String dest) {
        RouteRequirements requirements = new RouteRequirements(400, 50, "B737");
        return new Route(origin, dest, requirements, 300.0, 50);
    }

    @Test
    void shouldListRoutesByAirportSuccessfully() {
        // Arrange
        Route r1 = createRoute("LIS", "OPO");
        Route r2 = createRoute("MAD", "LIS");

        when(routeRepository.findByOriginOrDestination("LIS")).thenReturn(List.of(r1, r2));

        // Act
        List<RouteResponse> result = useCase.listByAirport("LIS");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("LIS", result.get(0).getOriginIataCode());
        assertEquals("LIS", result.get(1).getDestinationIataCode());
        verify(routeRepository, times(1)).findByOriginOrDestination("LIS");
    }

    @Test
    void shouldReturnEmptyListWhenNoRoutesExistForAirport() {
        // Arrange
        when(routeRepository.findByOriginOrDestination("OPO")).thenReturn(Collections.emptyList());

        // Act
        List<RouteResponse> result = useCase.listByAirport("OPO");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(routeRepository, times(1)).findByOriginOrDestination("OPO");
    }
}
