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
class SearchRoutesUseCaseTest {

    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private SearchRoutesUseCase useCase;

    private Route createRoute(String origin, String dest) {
        RouteRequirements requirements = new RouteRequirements(400, 50, "B737");
        return new Route(origin, dest, requirements, 300.0, 50);
    }

    @Test
    void shouldSearchRoutesWithBothOriginAndDestination() {
        // Arrange
        Route route = createRoute("LIS", "OPO");
        when(routeRepository.search("LIS", "OPO")).thenReturn(List.of(route));

        // Act
        List<RouteResponse> result = useCase.search("LIS", "OPO");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("LIS", result.get(0).getOriginIataCode());
        assertEquals("OPO", result.get(0).getDestinationIataCode());
        verify(routeRepository, times(1)).search("LIS", "OPO");
    }

    @Test
    void shouldSearchRoutesWithOnlyOriginAndEmptyDestination() {
        // Arrange
        Route route = createRoute("LIS", "OPO");
        when(routeRepository.search("LIS", null)).thenReturn(List.of(route));

        // Act
        List<RouteResponse> result = useCase.search("LIS", "");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(routeRepository, times(1)).search("LIS", null);
    }

    @Test
    void shouldSearchRoutesWithOnlyDestinationAndNullOrigin() {
        // Arrange
        Route route = createRoute("LIS", "OPO");
        when(routeRepository.search(null, "OPO")).thenReturn(List.of(route));

        // Act
        List<RouteResponse> result = useCase.search(null, "OPO");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(routeRepository, times(1)).search(null, "OPO");
    }

    @Test
    void shouldSearchRoutesWithNeitherOriginNorDestination() {
        // Arrange
        when(routeRepository.search(null, null)).thenReturn(Collections.emptyList());

        // Act
        List<RouteResponse> result = useCase.search("   ", null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(routeRepository, times(1)).search(null, null);
    }
}
