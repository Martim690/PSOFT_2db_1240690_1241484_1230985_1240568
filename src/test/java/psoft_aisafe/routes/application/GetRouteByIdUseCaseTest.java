package psoft_aisafe.routes.application;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import psoft_aisafe.routes.application.dtos.RouteResponse;
import psoft_aisafe.routes.application.dtos.RouteVersionResponse;
import psoft_aisafe.routes.domain.Route;
import psoft_aisafe.routes.domain.RouteID;
import psoft_aisafe.routes.domain.RouteRepository;
import psoft_aisafe.routes.domain.RouteRequirements;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetRouteByIdUseCaseTest {

    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private GetRouteByIdUseCase useCase;

    private Route createRoute() {
        RouteRequirements requirements = new RouteRequirements(400, 50, "B737");
        return new Route("LIS", "OPO", requirements, 300.0, 50);
    }

    @Test
    void shouldGetRouteByIdSuccessfully() {
        // Arrange
        Route route = createRoute();
        String routeIdStr = route.getRouteId().getRouteId();
        when(routeRepository.findByRouteId(RouteID.of(routeIdStr))).thenReturn(Optional.of(route));

        // Act
        RouteResponse response = useCase.getById(routeIdStr);

        // Assert
        assertNotNull(response);
        assertEquals(routeIdStr, response.getRouteId());
        assertEquals("LIS", response.getOriginIataCode());
        assertEquals("OPO", response.getDestinationIataCode());
        verify(routeRepository, times(1)).findByRouteId(RouteID.of(routeIdStr));
    }

    @Test
    void shouldFailToGetRouteByIdWhenNotFound() {
        // Arrange
        String routeIdStr = "non-existent";
        when(routeRepository.findByRouteId(RouteID.of(routeIdStr))).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            useCase.getById(routeIdStr);
        });

        assertEquals("Route not found: " + routeIdStr, exception.getMessage());
        verify(routeRepository, times(1)).findByRouteId(RouteID.of(routeIdStr));
    }

    @Test
    void shouldGetHistorySuccessfully() {
        // Arrange
        Route route = createRoute();
        String routeIdStr = route.getRouteId().getRouteId();
        when(routeRepository.findByRouteId(RouteID.of(routeIdStr))).thenReturn(Optional.of(route));

        // Act
        List<RouteVersionResponse> history = useCase.getHistory(routeIdStr);

        // Assert
        assertNotNull(history);
        assertEquals(1, history.size());
        assertEquals(300.0, history.get(0).getDistance());
        verify(routeRepository, times(1)).findByRouteId(RouteID.of(routeIdStr));
    }

    @Test
    void shouldFailToGetHistoryWhenNotFound() {
        // Arrange
        String routeIdStr = "non-existent";
        when(routeRepository.findByRouteId(RouteID.of(routeIdStr))).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            useCase.getHistory(routeIdStr);
        });

        assertEquals("Route not found: " + routeIdStr, exception.getMessage());
        verify(routeRepository, times(1)).findByRouteId(RouteID.of(routeIdStr));
    }
}
