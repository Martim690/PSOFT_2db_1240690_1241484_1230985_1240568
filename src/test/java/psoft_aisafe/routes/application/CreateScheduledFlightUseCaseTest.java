package psoft_aisafe.routes.application;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import psoft_aisafe.routes.application.dtos.CreateScheduledFlightRequest;
import psoft_aisafe.routes.application.dtos.ScheduledFlightResponse;
import psoft_aisafe.routes.domain.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateScheduledFlightUseCaseTest {

    @Mock
    private ScheduledFlightRepository scheduledFlightRepository;

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private AircraftGateway aircraftGateway;

    @InjectMocks
    private CreateScheduledFlightUseCase useCase;

    private CreateScheduledFlightRequest createValidRequest() {
        return new CreateScheduledFlightRequest(
                "route-123",
                "CS-REG1",
                LocalDateTime.now().plusDays(1)
        );
    }

    private Route createValidRoute() {
        RouteRequirements requirements = new RouteRequirements(500, 100, "B737");
        return new Route("LIS", "OPO", requirements, 300.0, 50);
    }

    private AircraftGateway.AircraftInfo createValidAircraft() {
        return new AircraftGateway.AircraftInfo(
                "CS-REG1",
                false,
                1000.0,
                150
        );
    }

    @Test
    void shouldCreateScheduledFlightSuccessfully() {
        // Arrange
        CreateScheduledFlightRequest request = createValidRequest();
        Route route = createValidRoute();
        AircraftGateway.AircraftInfo aircraft = createValidAircraft();

        when(routeRepository.findByRouteId(RouteID.of(request.routeId()))).thenReturn(Optional.of(route));
        when(aircraftGateway.findByRegistration(request.aircraftRegistration())).thenReturn(Optional.of(aircraft));
        when(scheduledFlightRepository.findByAircraftRegistrationAndScheduledDateTimeBetween(
                eq(request.aircraftRegistration()), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        when(scheduledFlightRepository.save(any(ScheduledFlight.class))).thenAnswer(invocation -> {
            ScheduledFlight sf = invocation.getArgument(0);
            // Simulate database setting an ID
            return sf;
        });

        // Act
        ScheduledFlightResponse response = useCase.execute(request);

        // Assert
        assertNotNull(response);
        assertEquals(request.routeId(), response.routeId());
        assertEquals(request.aircraftRegistration(), response.aircraftRegistration());
        assertEquals(request.scheduledDateTime(), response.scheduledDateTime());
        assertEquals(ScheduledFlightStatus.SCHEDULED, response.status());

        verify(routeRepository, times(1)).findByRouteId(RouteID.of(request.routeId()));
        verify(aircraftGateway, times(1)).findByRegistration(request.aircraftRegistration());
        verify(scheduledFlightRepository, times(1)).findByAircraftRegistrationAndScheduledDateTimeBetween(
                eq(request.aircraftRegistration()), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(scheduledFlightRepository, times(1)).save(any(ScheduledFlight.class));
    }

    @Test
    void shouldFailIfRouteNotFound() {
        // Arrange
        CreateScheduledFlightRequest request = createValidRequest();
        when(routeRepository.findByRouteId(RouteID.of(request.routeId()))).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            useCase.execute(request);
        });

        assertTrue(exception.getMessage().contains("Rota não encontrada:"));
        verify(aircraftGateway, never()).findByRegistration(anyString());
        verify(scheduledFlightRepository, never()).save(any(ScheduledFlight.class));
    }

    @Test
    void shouldFailIfRouteIsInactive() {
        // Arrange
        CreateScheduledFlightRequest request = createValidRequest();
        Route route = createValidRoute();
        route.deactivate();

        when(routeRepository.findByRouteId(RouteID.of(request.routeId()))).thenReturn(Optional.of(route));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            useCase.execute(request);
        });

        assertEquals("Rota não está ativa", exception.getMessage());
        verify(aircraftGateway, never()).findByRegistration(anyString());
        verify(scheduledFlightRepository, never()).save(any(ScheduledFlight.class));
    }

    @Test
    void shouldFailIfRouteHasNoCurrentVersion() {
        // Arrange
        CreateScheduledFlightRequest request = createValidRequest();
        Route route = createValidRoute();
        route.getCurrentVersion().ifPresent(v -> v.closeVersion(LocalDateTime.now()));

        when(routeRepository.findByRouteId(RouteID.of(request.routeId()))).thenReturn(Optional.of(route));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            useCase.execute(request);
        });

        assertTrue(exception.getMessage().contains("Rota sem versão ativa:"));
        verify(aircraftGateway, never()).findByRegistration(anyString());
        verify(scheduledFlightRepository, never()).save(any(ScheduledFlight.class));
    }

    @Test
    void shouldFailIfAircraftNotFound() {
        // Arrange
        CreateScheduledFlightRequest request = createValidRequest();
        Route route = createValidRoute();

        when(routeRepository.findByRouteId(RouteID.of(request.routeId()))).thenReturn(Optional.of(route));
        when(aircraftGateway.findByRegistration(request.aircraftRegistration())).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            useCase.execute(request);
        });

        assertTrue(exception.getMessage().contains("Aeronave não encontrada:"));
        verify(scheduledFlightRepository, never()).findByAircraftRegistrationAndScheduledDateTimeBetween(
                anyString(), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(scheduledFlightRepository, never()).save(any(ScheduledFlight.class));
    }

    @Test
    void shouldFailIfAircraftRangeIsInsufficient() {
        // Arrange
        CreateScheduledFlightRequest request = createValidRequest();
        Route route = createValidRoute(); // minRange = 500
        AircraftGateway.AircraftInfo aircraft = new AircraftGateway.AircraftInfo(
                "CS-REG1",
                false,
                400.0, // Insufficient range
                150
        );

        when(routeRepository.findByRouteId(RouteID.of(request.routeId()))).thenReturn(Optional.of(route));
        when(aircraftGateway.findByRegistration(request.aircraftRegistration())).thenReturn(Optional.of(aircraft));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            useCase.execute(request);
        });

        assertTrue(exception.getMessage().contains("Alcance insuficiente"));
        verify(scheduledFlightRepository, never()).findByAircraftRegistrationAndScheduledDateTimeBetween(
                anyString(), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(scheduledFlightRepository, never()).save(any(ScheduledFlight.class));
    }

    @Test
    void shouldFailIfAircraftIsUnderMaintenance() {
        // Arrange
        CreateScheduledFlightRequest request = createValidRequest();
        Route route = createValidRoute();
        AircraftGateway.AircraftInfo aircraft = new AircraftGateway.AircraftInfo(
                "CS-REG1",
                true, // Under maintenance
                1000.0,
                150
        );

        when(routeRepository.findByRouteId(RouteID.of(request.routeId()))).thenReturn(Optional.of(route));
        when(aircraftGateway.findByRegistration(request.aircraftRegistration())).thenReturn(Optional.of(aircraft));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            useCase.execute(request);
        });

        assertEquals("Aeronave em manutenção — não pode ser atribuída a voos", exception.getMessage());
        verify(scheduledFlightRepository, never()).findByAircraftRegistrationAndScheduledDateTimeBetween(
                anyString(), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(scheduledFlightRepository, never()).save(any(ScheduledFlight.class));
    }

    @Test
    void shouldFailIfAircraftHasSchedulingConflict() {
        // Arrange
        CreateScheduledFlightRequest request = createValidRequest();
        Route route = createValidRoute();
        AircraftGateway.AircraftInfo aircraft = createValidAircraft();

        when(routeRepository.findByRouteId(RouteID.of(request.routeId()))).thenReturn(Optional.of(route));
        when(aircraftGateway.findByRegistration(request.aircraftRegistration())).thenReturn(Optional.of(aircraft));

        ScheduledFlight existingFlight = new ScheduledFlight("route-other", "CS-REG1", LocalDateTime.now().plusDays(2));
        when(scheduledFlightRepository.findByAircraftRegistrationAndScheduledDateTimeBetween(
                eq(request.aircraftRegistration()), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(existingFlight));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            useCase.execute(request);
        });

        assertEquals("Aeronave já tem um voo agendado neste período", exception.getMessage());
        verify(scheduledFlightRepository, never()).save(any(ScheduledFlight.class));
    }

    @Test
    void shouldFailIfDepartureDateTimeIsInPast() {
        // Arrange
        CreateScheduledFlightRequest request = new CreateScheduledFlightRequest(
                "route-123",
                "CS-REG1",
                LocalDateTime.now().minusHours(1) // Departure in the past
        );
        Route route = createValidRoute();
        AircraftGateway.AircraftInfo aircraft = createValidAircraft();

        when(routeRepository.findByRouteId(RouteID.of(request.routeId()))).thenReturn(Optional.of(route));
        when(aircraftGateway.findByRegistration(request.aircraftRegistration())).thenReturn(Optional.of(aircraft));
        when(scheduledFlightRepository.findByAircraftRegistrationAndScheduledDateTimeBetween(
                eq(request.aircraftRegistration()), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            useCase.execute(request);
        });

        assertEquals("Data/hora não pode ser no passado", exception.getMessage());
        verify(scheduledFlightRepository, never()).save(any(ScheduledFlight.class));
    }

    @Test
    void shouldFailIfRouteVersionEstimatedFlightTimeIsNegativeOrZero() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new RouteVersion(300.0, 0, LocalDateTime.now(), "Initial creation", null);
        });

        assertEquals("Estimated flight time must be positive", exception.getMessage());
    }
}
