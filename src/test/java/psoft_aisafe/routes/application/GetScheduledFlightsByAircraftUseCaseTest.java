package psoft_aisafe.routes.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import psoft_aisafe.routes.application.dtos.ScheduledFlightResponse;
import psoft_aisafe.routes.domain.ScheduledFlight;
import psoft_aisafe.routes.domain.ScheduledFlightRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetScheduledFlightsByAircraftUseCaseTest {

    @Mock
    private ScheduledFlightRepository scheduledFlightRepository;

    @InjectMocks
    private GetScheduledFlightsByAircraftUseCase useCase;

    @Test
    void shouldGetScheduledFlightsByAircraftSuccessfully() {
        // Arrange
        String registration = "CS-REG1";
        ScheduledFlight sf1 = new ScheduledFlight("route-1", registration, LocalDateTime.now().plusDays(1));
        ScheduledFlight sf2 = new ScheduledFlight("route-2", registration, LocalDateTime.now().plusDays(2));

        when(scheduledFlightRepository.findByAircraftRegistration(registration)).thenReturn(List.of(sf1, sf2));

        // Act
        List<ScheduledFlightResponse> result = useCase.execute(registration);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("route-1", result.get(0).routeId());
        assertEquals("route-2", result.get(1).routeId());
        verify(scheduledFlightRepository, times(1)).findByAircraftRegistration(registration);
    }

    @Test
    void shouldReturnEmptyListWhenNoFlightsFound() {
        // Arrange
        String registration = "CS-REG1";
        when(scheduledFlightRepository.findByAircraftRegistration(registration)).thenReturn(Collections.emptyList());

        // Act
        List<ScheduledFlightResponse> result = useCase.execute(registration);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(scheduledFlightRepository, times(1)).findByAircraftRegistration(registration);
    }
}
