package psoft_aisafe.routes.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import psoft_aisafe.routes.application.dtos.CreateRouteRequest;
import psoft_aisafe.routes.application.dtos.RouteResponse;
import psoft_aisafe.routes.domain.Route;
import psoft_aisafe.routes.domain.RouteRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateRouteUseCaseTest {

    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private CreateRouteUseCase createRouteUseCase;

    private CreateRouteRequest createValidRequest() {
        CreateRouteRequest request = new CreateRouteRequest();
        request.setOriginIataCode("LIS");
        request.setDestinationIataCode("OPO");
        request.setEstimatedFlightTime(50);
        request.setDistance(300.0);
        request.setMinimumRange(400);
        request.setMinimumCapacity(50);
        request.setRequiredCertificationCode("B737");
        return request;
    }

    @Test
    void shouldCreateRouteSuccessfully() {
        // Arrange
        CreateRouteRequest request = createValidRequest();
        when(routeRepository.existsActiveRouteBetween("LIS", "OPO")).thenReturn(false);
        when(routeRepository.save(any(Route.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        RouteResponse response = createRouteUseCase.create(request);

        // Assert
        assertNotNull(response);
        assertEquals("LIS", response.getOriginIataCode());
        assertEquals("OPO", response.getDestinationIataCode());
        assertTrue(response.isActive());
        assertEquals(400, response.getMinimumRange());
        assertEquals(50, response.getMinimumCapacity());
        assertEquals("B737", response.getRequiredCertificationCode());
        assertEquals(300.0, response.getCurrentDistance());
        assertEquals(50, response.getCurrentEstimatedFlightTime());

        verify(routeRepository, times(1)).existsActiveRouteBetween("LIS", "OPO");
        verify(routeRepository, times(1)).save(any(Route.class));
    }

    @Test
    void shouldFailIfActiveRouteAlreadyExists() {
        // Arrange
        CreateRouteRequest request = createValidRequest();
        when(routeRepository.existsActiveRouteBetween("LIS", "OPO")).thenReturn(true);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            createRouteUseCase.create(request);
        });

        assertEquals("An active route already exists between LIS and OPO", exception.getMessage());
        verify(routeRepository, times(1)).existsActiveRouteBetween("LIS", "OPO");
        verify(routeRepository, never()).save(any(Route.class));
    }

    @Test
    void shouldFailIfMinimumRangeIsNegativeOrZero() {
        // Arrange
        CreateRouteRequest request = createValidRequest();
        request.setMinimumRange(0);
        when(routeRepository.existsActiveRouteBetween("LIS", "OPO")).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createRouteUseCase.create(request);
        });

        assertEquals("Minimum range must be positive", exception.getMessage());
        verify(routeRepository, never()).save(any(Route.class));
    }

    @Test
    void shouldFailIfMinimumCapacityIsNegativeOrZero() {
        // Arrange
        CreateRouteRequest request = createValidRequest();
        request.setMinimumCapacity(-5);
        when(routeRepository.existsActiveRouteBetween("LIS", "OPO")).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createRouteUseCase.create(request);
        });

        assertEquals("Minimum capacity must be positive", exception.getMessage());
        verify(routeRepository, never()).save(any(Route.class));
    }

    @Test
    void shouldFailIfOriginAndDestinationAreSame() {
        // Arrange
        CreateRouteRequest request = createValidRequest();
        request.setDestinationIataCode("LIS");
        when(routeRepository.existsActiveRouteBetween("LIS", "LIS")).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createRouteUseCase.create(request);
        });

        assertEquals("Origin and destination cannot be the same airport", exception.getMessage());
        verify(routeRepository, never()).save(any(Route.class));
    }

    @Test
    void shouldFailIfOriginIsNull() {
        // Arrange
        CreateRouteRequest request = createValidRequest();
        request.setOriginIataCode(null);
        when(routeRepository.existsActiveRouteBetween(null, "OPO")).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createRouteUseCase.create(request);
        });

        assertEquals("Origin IATA code cannot be null", exception.getMessage());
        verify(routeRepository, never()).save(any(Route.class));
    }

    @Test
    void shouldFailIfDestinationIsNull() {
        // Arrange
        CreateRouteRequest request = createValidRequest();
        request.setDestinationIataCode(null);
        when(routeRepository.existsActiveRouteBetween("LIS", null)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createRouteUseCase.create(request);
        });

        assertEquals("Destination IATA code cannot be null", exception.getMessage());
        verify(routeRepository, never()).save(any(Route.class));
    }

    @Test
    void shouldFailIfDistanceIsNegativeOrZero() {
        // Arrange
        CreateRouteRequest request = createValidRequest();
        request.setDistance(-10.0);
        when(routeRepository.existsActiveRouteBetween("LIS", "OPO")).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createRouteUseCase.create(request);
        });

        assertEquals("Distance must be positive", exception.getMessage());
        verify(routeRepository, never()).save(any(Route.class));
    }

    @Test
    void shouldFailIfEstimatedFlightTimeIsNegativeOrZero() {
        // Arrange
        CreateRouteRequest request = createValidRequest();
        request.setEstimatedFlightTime(0);
        when(routeRepository.existsActiveRouteBetween("LIS", "OPO")).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createRouteUseCase.create(request);
        });

        assertEquals("Estimated flight time must be positive", exception.getMessage());
        verify(routeRepository, never()).save(any(Route.class));
    }
}
