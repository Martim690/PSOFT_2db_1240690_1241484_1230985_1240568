package psoft_aisafe.aircrafts.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import psoft_aisafe.aircrafts.application.dtos.AircraftResponse;
import psoft_aisafe.aircrafts.domain.*;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAircraftByRegistrationUseCaseTest {

    @Mock private AircraftRepository aircraftRepository;
    @InjectMocks private GetAircraftByRegistrationUseCase useCase;

    @Test
    void shouldThrowExceptionWhenAircraftIsNotFound() {
        when(aircraftRepository.findByRegistrationNumber(any(RegistrationNumber.class)))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> useCase.execute("CS-ZZZ"));

        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void shouldReturnAircraftResponseWhenFound() {
        Aircraft mockAircraft = mock(Aircraft.class);
        RegistrationNumber mockReg = new RegistrationNumber("CS-TPA");
        AircraftModel mockModel = mock(AircraftModel.class);

        when(mockAircraft.getRegistrationNumber()).thenReturn(mockReg);
        when(mockAircraft.getModel()).thenReturn(mockModel);
        when(mockModel.getModelName()).thenReturn("B737");

        when(mockAircraft.getManufacturingDate()).thenReturn(LocalDate.now());
        when(mockAircraft.getSeatingCapacity()).thenReturn(150);
        when(mockAircraft.getCurrentStatus()).thenReturn(AircraftStatus.AVAILABLE);

        when(aircraftRepository.findByRegistrationNumber(any(RegistrationNumber.class)))
                .thenReturn(Optional.of(mockAircraft));

        AircraftResponse response = useCase.execute("CS-TPA");

        assertNotNull(response);
        assertEquals("CS-TPA", response.registrationNumber());
    }
}