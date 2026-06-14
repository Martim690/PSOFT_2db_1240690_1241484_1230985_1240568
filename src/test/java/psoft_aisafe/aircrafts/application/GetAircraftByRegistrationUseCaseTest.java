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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAircraftByRegistrationUseCaseTest {

    @Mock
    private AircraftRepository repository;

    @InjectMocks
    private GetAircraftByRegistrationUseCase useCase;

    @Test
    void shouldReturnAircraftDetailsSuccessfully() {
        AircraftModel model = new AircraftModel("B737", 100, 100, 100, AircraftManufacturer.BOEING, null); // Diagram null
        Aircraft aircraft = new Aircraft(new RegistrationNumber("CS-TPA"), model, LocalDate.now(), 150, AircraftStatus.AVAILABLE);

        when(repository.findByRegistrationNumber(any(RegistrationNumber.class))).thenReturn(Optional.of(aircraft));

        AircraftResponse result = useCase.execute("CS-TPA");

        assertEquals("CS-TPA", result.registrationNumber());
        assertEquals("AVAILABLE", result.currentStatus());
    }

    @Test
    void shouldThrowExceptionWhenNotFound() {
        when(repository.findByRegistrationNumber(any(RegistrationNumber.class))).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> useCase.execute("UNKNOWN"));
    }
}