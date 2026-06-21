package psoft_aisafe.routes.infrastructure;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import psoft_aisafe.routes.application.*;
import psoft_aisafe.routes.application.dtos.*;
import psoft_aisafe.routes.domain.Route;
import psoft_aisafe.routes.domain.RouteRequirements;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RouteControllerTest {

    @Mock private CreateRouteUseCase createRouteUseCase;
    @Mock private UpdateRouteUseCase updateRouteUseCase;
    @Mock private DeactivateRouteUseCase deactivateRouteUseCase;
    @Mock private GetRouteByIdUseCase getRouteByIdUseCase;
    @Mock private ListRoutesByAirportUseCase listRoutesByAirportUseCase;
    @Mock private SearchRoutesUseCase searchRoutesUseCase;
    @Mock private ListActiveRoutesSortedUseCase listActiveRoutesSortedUseCase;
    @Mock private GetTotalNetworkDistanceUseCase getTotalNetworkDistanceUseCase;
    @Mock private SearchAlternativeRoutesUseCase searchAlternativeRoutesUseCase;

    @InjectMocks
    private RouteController controller;

    private CreateRouteRequest createValidRouteRequest() {
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

    private RouteResponse createMockRouteResponse() {
        Route route = new Route("LIS", "OPO", new RouteRequirements(400, 50, "B737"), 300.0, 50);
        return RouteResponse.from(route);
    }

    @Test
    void shouldReturn201CreatedWhenCreatingRoute() {
        // Arrange
        CreateRouteRequest request = createValidRouteRequest();
        RouteResponse expectedResponse = createMockRouteResponse();

        when(createRouteUseCase.create(any(CreateRouteRequest.class))).thenReturn(expectedResponse);

        // Act
        ResponseEntity<RouteResponse> response = controller.create(request);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("LIS", response.getBody().getOriginIataCode());
        assertEquals("OPO", response.getBody().getDestinationIataCode());
        verify(createRouteUseCase, times(1)).create(request);
    }

    @Test
    void shouldReturn200OkWhenUpdatingRoute() {
        // Arrange
        UpdateRouteRequest request = new UpdateRouteRequest();
        request.setDistance(310.0);
        request.setEstimatedFlightTime(55);
        request.setMinimumRange(500);
        request.setMinimumCapacity(80);
        request.setRequiredCertificationCode("A320");
        request.setChangeReason("Specs update");

        RouteResponse expectedResponse = createMockRouteResponse();

        when(updateRouteUseCase.update(eq("route-123"), any(UpdateRouteRequest.class))).thenReturn(expectedResponse);

        // Act
        ResponseEntity<RouteResponse> response = controller.update("route-123", request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(updateRouteUseCase, times(1)).update("route-123", request);
    }

    @Test
    void shouldReturn200OkWhenGettingRouteById() {
        // Arrange
        RouteResponse expectedResponse = createMockRouteResponse();
        when(getRouteByIdUseCase.getById("route-123")).thenReturn(expectedResponse);

        // Act
        ResponseEntity<RouteResponse> response = controller.getById("route-123");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedResponse.getRouteId(), response.getBody().getRouteId());
        verify(getRouteByIdUseCase, times(1)).getById("route-123");
    }

    @Test
    void shouldReturn204NoContentWhenDeactivatingRoute() {
        // Act
        ResponseEntity<Void> response = controller.deactivate("route-123");

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(deactivateRouteUseCase, times(1)).deactivate("route-123");
    }
}