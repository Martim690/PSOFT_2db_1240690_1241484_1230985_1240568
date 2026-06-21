package psoft_aisafe.routes.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import psoft_aisafe.routes.application.dtos.TotalNetworkDistanceResponse;
import psoft_aisafe.routes.domain.Route;
import psoft_aisafe.routes.domain.RouteRepository;
import psoft_aisafe.routes.domain.RouteRequirements;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetTotalNetworkDistanceUseCaseTest {

    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private GetTotalNetworkDistanceUseCase useCase;

    private Route createRoute(String origin, String dest, double distance) {
        RouteRequirements requirements = new RouteRequirements(400, 50, "B737");
        return new Route(origin, dest, requirements, distance, 50);
    }

    @Test
    void shouldCalculateTotalDistanceForMultipleActiveRoutes() {
        // Arrange
        Route r1 = createRoute("LIS", "OPO", 300.0);
        Route r2 = createRoute("LIS", "MAD", 500.5);
        Route r3 = createRoute("LIS", "LHR", 1200.25);

        when(routeRepository.findByActiveTrue()).thenReturn(List.of(r1, r2, r3));

        // Act
        TotalNetworkDistanceResponse response = useCase.execute();

        // Assert
        assertNotNull(response);
        assertEquals(2000.75, response.totalDistanceKm());
        assertEquals(3, response.activeRouteCount());
        verify(routeRepository, times(1)).findByActiveTrue();
    }

    @Test
    void shouldCalculateZeroDistanceWhenNoActiveRoutes() {
        // Arrange
        when(routeRepository.findByActiveTrue()).thenReturn(Collections.emptyList());

        // Act
        TotalNetworkDistanceResponse response = useCase.execute();

        // Assert
        assertNotNull(response);
        assertEquals(0.0, response.totalDistanceKm());
        assertEquals(0, response.activeRouteCount());
        verify(routeRepository, times(1)).findByActiveTrue();
    }

    @Test
    void shouldIgnoreDistanceIfRouteHasNoCurrentVersion() {
        // Arrange
        Route r1 = createRoute("LIS", "OPO", 300.0);
        r1.getCurrentVersion().ifPresent(v -> v.closeVersion(LocalDateTime.now()));

        when(routeRepository.findByActiveTrue()).thenReturn(List.of(r1));

        // Act
        TotalNetworkDistanceResponse response = useCase.execute();

        // Assert
        assertNotNull(response);
        assertEquals(0.0, response.totalDistanceKm());
        assertEquals(1, response.activeRouteCount());
        verify(routeRepository, times(1)).findByActiveTrue();
    }
}
