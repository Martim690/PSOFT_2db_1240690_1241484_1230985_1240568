package psoft_aisafe.aircrafts.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import psoft_aisafe.aircrafts.application.dtos.FleetStatusResponse;
import psoft_aisafe.aircrafts.domain.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetFleetStatusUseCaseTest {

    @Mock private AircraftRepository aircraftRepository;
    @InjectMocks private GetFleetStatusUseCase useCase;

    @Test
    void shouldReturnCorrectFleetCountsIncludingZeros() {
        AircraftModel model = new AircraftModel("M1", 1, 1, 1, AircraftManufacturer.BOEING, null);
        Aircraft a1 = new Aircraft(new RegistrationNumber("CS-AAA"), model, LocalDate.now(), 100, AircraftStatus.AVAILABLE);
        Aircraft a2 = new Aircraft(new RegistrationNumber("CS-BBB"), model, LocalDate.now(), 100, AircraftStatus.IN_FLIGHT);

        when(aircraftRepository.findAll()).thenReturn(List.of(a1, a2));

        FleetStatusResponse response = useCase.execute();

        assertEquals(2, response.totalAircraft());
        assertEquals(1L, response.statusCounts().get(AircraftStatus.AVAILABLE.name()));
        assertEquals(1L, response.statusCounts().get(AircraftStatus.IN_FLIGHT.name()));

        assertEquals(0L, response.statusCounts().get(AircraftStatus.UNDER_MAINTENANCE.name()));
    }
}