package psoft_aisafe.aircrafts.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import psoft_aisafe.aircrafts.application.dtos.RegisterAircraftRequest;
import psoft_aisafe.aircrafts.domain.*;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterAircraftUseCaseTest {

    @Mock private AircraftRepository aircraftRepository;
    @Mock private AircraftModelRepository modelRepository;
    @InjectMocks private RegisterAircraftUseCase useCase;

    @Test void throwsExceptionWhenAircraftAlreadyExists() {
        when(aircraftRepository.findByRegistrationNumber(any(RegistrationNumber.class))).thenReturn(Optional.of(mock(Aircraft.class)));
        RegisterAircraftRequest req = new RegisterAircraftRequest("CS-TPA", "B737", LocalDate.now(), 150, AircraftStatus.AVAILABLE);
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(req));
    }

    @Test void throwsExceptionWhenModelNotFound() {
        when(aircraftRepository.findByRegistrationNumber(any(RegistrationNumber.class))).thenReturn(Optional.empty());
        when(modelRepository.findByModelName("B737")).thenReturn(Optional.empty());
        RegisterAircraftRequest req = new RegisterAircraftRequest("CS-TPA", "B737", LocalDate.now(), 150, AircraftStatus.AVAILABLE);
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(req));
    }

    @Test void registersSuccessfully() {
        when(aircraftRepository.findByRegistrationNumber(any(RegistrationNumber.class))).thenReturn(Optional.empty());
        AircraftModel mockModel = mock(AircraftModel.class);
        lenient().when(mockModel.getModelName()).thenReturn("B737");
        lenient().when(modelRepository.findByModelName("B737")).thenReturn(Optional.of(mockModel));
        Aircraft mockSaved = mock(Aircraft.class);
        when(aircraftRepository.save(any(Aircraft.class))).thenReturn(mockSaved);
        RegisterAircraftRequest req = new RegisterAircraftRequest("CS-TPA", "B737", LocalDate.now(), 150, AircraftStatus.AVAILABLE);
        assertDoesNotThrow(() -> useCase.execute(req));
        verify(aircraftRepository).save(any(Aircraft.class));
    }

    @Test void doesNotSaveIfModelIsInvalid() {
        when(aircraftRepository.findByRegistrationNumber(any(RegistrationNumber.class))).thenReturn(Optional.empty());
        when(modelRepository.findByModelName("INVALID")).thenReturn(Optional.empty());
        RegisterAircraftRequest req = new RegisterAircraftRequest("CS-TPA", "INVALID", LocalDate.now(), 150, AircraftStatus.AVAILABLE);
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(req));
        verify(aircraftRepository, never()).save(any(Aircraft.class));
    }
}