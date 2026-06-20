package psoft_aisafe.aircrafts.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import psoft_aisafe.aircrafts.application.dtos.FleetStatusResponse;
import psoft_aisafe.aircrafts.domain.Aircraft;
import psoft_aisafe.aircrafts.domain.AircraftRepository;
import psoft_aisafe.aircrafts.domain.AircraftStatus;
import psoft_aisafe.aircrafts.domain.RegistrationNumber;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetFleetStatusUseCaseTest {

    @Mock
    private AircraftRepository aircraftRepository;

    @InjectMocks
    private GetFleetStatusUseCase useCase;

    @Test
    void shouldReturnFleetStatusCountsAndDetails() {
        Aircraft a1 = mock(Aircraft.class);
        RegistrationNumber r1 = new RegistrationNumber("CS-TXA");
        when(a1.getRegistrationNumber()).thenReturn(r1);
        when(a1.getCurrentStatus()).thenReturn(AircraftStatus.AVAILABLE);

        Aircraft a2 = mock(Aircraft.class);
        RegistrationNumber r2 = new RegistrationNumber("CS-TZE");
        when(a2.getRegistrationNumber()).thenReturn(r2);
        when(a2.getCurrentStatus()).thenReturn(AircraftStatus.IN_FLIGHT);

        when(aircraftRepository.findAll()).thenReturn(List.of(a1, a2));

        FleetStatusResponse response = useCase.execute();

        assertEquals(1L, response.statusCounts().get("AVAILABLE"));
        assertEquals(1L, response.statusCounts().get("IN_FLIGHT"));

        assertEquals(2, response.aircrafts().size());
        assertEquals("CS-TXA", response.aircrafts().get(0).registrationNumber());
        assertEquals("AVAILABLE", response.aircrafts().get(0).status());
    }
}