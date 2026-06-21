package psoft_aisafe.routes.application;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import psoft_aisafe.routes.application.dtos.RouteResponse;
import psoft_aisafe.routes.application.dtos.UpdateRouteRequest;
import psoft_aisafe.routes.domain.Route;
import psoft_aisafe.routes.domain.RouteID;
import psoft_aisafe.routes.domain.RouteRepository;
import psoft_aisafe.routes.domain.RouteRequirements;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateRouteUseCaseTest {

    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private UpdateRouteUseCase useCase;

    private UpdateRouteRequest createValidRequest() {
        UpdateRouteRequest request = new UpdateRouteRequest();
        request.setDistance(310.0);
        request.setEstimatedFlightTime(55);
        request.setMinimumRange(500);
        request.setMinimumCapacity(80);
        request.setRequiredCertificationCode("A320");
        request.setChangeReason("Upgraded aircraft requirement");
        return request;
    }

    private Route createRoute() {
        RouteRequirements requirements = new RouteRequirements(400, 50, "B737");
        return new Route("LIS", "OPO", requirements, 300.0, 50);
    }

    @Test
    void shouldUpdateRouteSuccessfully() {
        // Arrange
        Route route = createRoute();
        String routeIdStr = route.getRouteId().getRouteId();
        UpdateRouteRequest request = createValidRequest();

        when(routeRepository.findByRouteId(RouteID.of(routeIdStr))).thenReturn(Optional.of(route));
        when(routeRepository.save(any(Route.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        RouteResponse response = useCase.update(routeIdStr, request);

        // Assert
        assertNotNull(response);
        assertEquals(routeIdStr, response.getRouteId());
        // Confirm mutable properties are updated
        assertEquals(500, response.getMinimumRange());
        assertEquals(80, response.getMinimumCapacity());
        assertEquals("A320", response.getRequiredCertificationCode());
        assertEquals(310.0, response.getCurrentDistance());
        assertEquals(55, response.getCurrentEstimatedFlightTime());
        // Confirm immutable properties (origin/destination) remain unchanged (no data corruption)
        assertEquals("LIS", response.getOriginIataCode());
        assertEquals("OPO", response.getDestinationIataCode());

        // Verify route has 2 versions (history is preserved)
        assertEquals(2, route.getVersions().size());

        verify(routeRepository, times(1)).findByRouteId(RouteID.of(routeIdStr));
        verify(routeRepository, times(1)).save(any(Route.class));
    }

    @Test
    void shouldFailIfRouteNotFound() {
        // Arrange
        UpdateRouteRequest request = createValidRequest();
        when(routeRepository.findByRouteId(any(RouteID.class))).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            useCase.update("non-existent", request);
        });

        assertEquals("Route not found: non-existent", exception.getMessage());
        verify(routeRepository, never()).save(any(Route.class));
    }

    @Test
    void shouldFailIfRouteIsInactive() {
        // Arrange
        Route route = createRoute();
        route.deactivate();
        String routeIdStr = route.getRouteId().getRouteId();
        UpdateRouteRequest request = createValidRequest();

        when(routeRepository.findByRouteId(RouteID.of(routeIdStr))).thenReturn(Optional.of(route));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            useCase.update(routeIdStr, request);
        });

        assertEquals("Cannot update an inactive route", exception.getMessage());
        verify(routeRepository, never()).save(any(Route.class));
    }

    @Test
    void shouldNotAllowAlteringOriginAndDestinationSinceTheyAreImmutable() {
        // Arrange
        Route route = createRoute();
        String routeIdStr = route.getRouteId().getRouteId();
        UpdateRouteRequest request = createValidRequest();

        when(routeRepository.findByRouteId(RouteID.of(routeIdStr))).thenReturn(Optional.of(route));
        when(routeRepository.save(any(Route.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        RouteResponse response = useCase.update(routeIdStr, request);

        // Assert
        // Origin and destination cannot be changed via UpdateRouteRequest as it lacks these fields,
        // and the Route domain class does not expose setters or update mechanisms for origin and destination.
        // Therefore, we ensure no duplication of active routes is possible by altering an existing route's endpoints.
        assertEquals("LIS", response.getOriginIataCode());
        assertEquals("OPO", response.getDestinationIataCode());
    }
}
