package psoft_aisafe.routes.application;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import psoft_aisafe.routes.domain.Route;
import psoft_aisafe.routes.domain.RouteID;
import psoft_aisafe.routes.domain.RouteRepository;
import psoft_aisafe.routes.domain.RouteRequirements;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeactivateRouteUseCaseTest {

    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private DeactivateRouteUseCase useCase;

    private Route createRoute() {
        RouteRequirements requirements = new RouteRequirements(400, 50, "B737");
        return new Route("LIS", "OPO", requirements, 300.0, 50);
    }

    @Test
    void shouldDeactivateRouteSuccessfully() {
        // Arrange
        Route route = createRoute();
        String routeIdStr = route.getRouteId().getRouteId();

        when(routeRepository.findByRouteId(RouteID.of(routeIdStr))).thenReturn(Optional.of(route));
        when(routeRepository.save(any(Route.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        useCase.deactivate(routeIdStr);

        // Assert
        assertFalse(route.isActive());
        verify(routeRepository, times(1)).findByRouteId(RouteID.of(routeIdStr));
        verify(routeRepository, times(1)).save(route);
    }

    @Test
    void shouldFailIfRouteNotFound() {
        // Arrange
        String routeIdStr = "non-existent";
        when(routeRepository.findByRouteId(RouteID.of(routeIdStr))).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            useCase.deactivate(routeIdStr);
        });

        assertEquals("Route not found: " + routeIdStr, exception.getMessage());
        verify(routeRepository, never()).save(any(Route.class));
    }

    @Test
    void shouldFailIfRouteIsAlreadyInactive() {
        // Arrange
        Route route = createRoute();
        route.deactivate(); // Already inactive
        String routeIdStr = route.getRouteId().getRouteId();

        when(routeRepository.findByRouteId(RouteID.of(routeIdStr))).thenReturn(Optional.of(route));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            useCase.deactivate(routeIdStr);
        });

        assertEquals("Route is already inactive", exception.getMessage());
        verify(routeRepository, never()).save(any(Route.class));
    }
}
