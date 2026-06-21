package psoft_aisafe.aircrafts.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import psoft_aisafe.aircrafts.domain.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAircraftByRegistrationUseCaseTest {

    @Mock private AircraftRepository repository;
    @InjectMocks private GetAircraftByRegistrationUseCase useCase;

    @Test void throwsExceptionWhenNotFound() {
        when(repository.findByRegistrationNumber(any(RegistrationNumber.class))).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> useCase.execute("CS-ZZZ"));
    }

    @Test void returnsAircraftWhenFound() {
        Aircraft mockAircraft = mock(Aircraft.class);
        RegistrationNumber mockReg = mock(RegistrationNumber.class);
        AircraftModel mockModel = mock(AircraftModel.class);
        when(mockAircraft.getRegistrationNumber()).thenReturn(mockReg);
        when(mockReg.getNumber()).thenReturn("CS-TPA");
        when(mockAircraft.getModel()).thenReturn(mockModel);
        when(mockModel.getModelName()).thenReturn("B737");
        when(mockAircraft.getCurrentStatus()).thenReturn(AircraftStatus.AVAILABLE);
        when(repository.findByRegistrationNumber(any(RegistrationNumber.class))).thenReturn(Optional.of(mockAircraft));
        var result = useCase.execute("CS-TPA");
        assertNotNull(result);
    }

    @Test void callsRepositoryWithCorrectRegistration() {
        when(repository.findByRegistrationNumber(any(RegistrationNumber.class))).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> useCase.execute("CS-ABC"));
        verify(repository, times(1)).findByRegistrationNumber(any(RegistrationNumber.class));
    }
}