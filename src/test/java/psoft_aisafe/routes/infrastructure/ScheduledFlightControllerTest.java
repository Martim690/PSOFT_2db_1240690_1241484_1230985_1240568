package psoft_aisafe.routes.infrastructure;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import psoft_aisafe.routes.application.CreateScheduledFlightUseCase;
import psoft_aisafe.routes.application.GetScheduledFlightsByAircraftUseCase;
import psoft_aisafe.routes.application.dtos.CreateScheduledFlightRequest;
import psoft_aisafe.routes.application.dtos.ScheduledFlightResponse;
import psoft_aisafe.routes.domain.ScheduledFlightStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduledFlightControllerTest {

    @Mock
    private CreateScheduledFlightUseCase createUseCase;

    @Mock
    private GetScheduledFlightsByAircraftUseCase getByAircraftUseCase;

    @InjectMocks
    private ScheduledFlightController controller;

    @Test
    void shouldReturn201CreatedWhenSchedulingFlight() {
        // Arrange
        LocalDateTime futureDateTime = LocalDateTime.now().plusDays(1);
        CreateScheduledFlightRequest request = new CreateScheduledFlightRequest("route-123", "CS-REG1", futureDateTime);
        ScheduledFlightResponse expectedResponse = new ScheduledFlightResponse(
                1L, "route-123", "CS-REG1", futureDateTime, ScheduledFlightStatus.SCHEDULED
        );

        when(createUseCase.execute(any(CreateScheduledFlightRequest.class))).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ScheduledFlightResponse> response = controller.create(request);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("route-123", response.getBody().routeId());
        assertEquals("CS-REG1", response.getBody().aircraftRegistration());
        verify(createUseCase, times(1)).execute(request);
    }

    @Test
    void shouldReturn200OkWhenGettingFlightsByAircraft() {
        // Arrange
        LocalDateTime futureDateTime = LocalDateTime.now().plusDays(1);
        ScheduledFlightResponse expectedResponse = new ScheduledFlightResponse(
                1L, "route-123", "CS-REG1", futureDateTime, ScheduledFlightStatus.SCHEDULED
        );
        when(getByAircraftUseCase.execute("CS-REG1")).thenReturn(List.of(expectedResponse));

        // Act
        ResponseEntity<List<ScheduledFlightResponse>> response = controller.getByAircraft("CS-REG1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("CS-REG1", response.getBody().get(0).aircraftRegistration());
        verify(getByAircraftUseCase, times(1)).execute("CS-REG1");
    }
}