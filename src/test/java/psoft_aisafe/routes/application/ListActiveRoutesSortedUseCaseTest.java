package psoft_aisafe.routes.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import psoft_aisafe.routes.application.dtos.RouteWithStatsDTO;
import psoft_aisafe.routes.domain.Route;
import psoft_aisafe.routes.domain.RouteRepository;
import psoft_aisafe.routes.domain.RouteRequirements;
import psoft_aisafe.routes.domain.ScheduledFlightRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListActiveRoutesSortedUseCaseTest {

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private ScheduledFlightRepository scheduledFlightRepository;

    @InjectMocks
    private ListActiveRoutesSortedUseCase useCase;

    private Route createRoute(String origin, String dest, double distance) {
        RouteRequirements requirements = new RouteRequirements(400, 50, "B737");
        return new Route(origin, dest, requirements, distance, 50);
    }

    @Test
    void shouldListAndSortActiveRoutesByPopularityByDefault() {
        // Arrange
        Route r1 = createRoute("LIS", "OPO", 300.0); // popularity 5
        Route r2 = createRoute("LIS", "MAD", 500.0); // popularity 12
        Route r3 = createRoute("LIS", "LHR", 1000.0); // popularity 2

        when(routeRepository.findByActiveTrue()).thenReturn(List.of(r1, r2, r3));
        when(scheduledFlightRepository.countByRouteId(r1.getRouteId().getRouteId())).thenReturn(5L);
        when(scheduledFlightRepository.countByRouteId(r2.getRouteId().getRouteId())).thenReturn(12L);
        when(scheduledFlightRepository.countByRouteId(r3.getRouteId().getRouteId())).thenReturn(2L);

        // Act
        List<RouteWithStatsDTO> result = useCase.execute("popularity");

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        // Ordered by popularity (usageCount) descending: r2 (12), r1 (5), r3 (2)
        assertEquals(r2.getRouteId().getRouteId(), result.get(0).routeId());
        assertEquals(r1.getRouteId().getRouteId(), result.get(1).routeId());
        assertEquals(r3.getRouteId().getRouteId(), result.get(2).routeId());

        verify(routeRepository, times(1)).findByActiveTrue();
        verify(scheduledFlightRepository, times(3)).countByRouteId(anyString());
    }

    @Test
    void shouldListAndSortActiveRoutesByDistance() {
        // Arrange
        Route r1 = createRoute("LIS", "OPO", 300.0);
        Route r2 = createRoute("LIS", "MAD", 500.0);
        Route r3 = createRoute("LIS", "LHR", 1000.0);

        when(routeRepository.findByActiveTrue()).thenReturn(List.of(r3, r1, r2)); // Mixed order from repo
        when(scheduledFlightRepository.countByRouteId(anyString())).thenReturn(0L);

        // Act
        List<RouteWithStatsDTO> result = useCase.execute("distance");

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        // Ordered by distance ascending: r1 (300.0), r2 (500.0), r3 (1000.0)
        assertEquals(r1.getRouteId().getRouteId(), result.get(0).routeId());
        assertEquals(r2.getRouteId().getRouteId(), result.get(1).routeId());
        assertEquals(r3.getRouteId().getRouteId(), result.get(2).routeId());
    }
}
