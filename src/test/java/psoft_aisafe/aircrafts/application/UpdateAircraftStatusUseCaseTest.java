package psoft_aisafe.aircrafts.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import psoft_aisafe.aircrafts.application.dtos.UpdateAircraftStatusRequest;
import psoft_aisafe.aircrafts.domain.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateAircraftStatusUseCaseTest {

    @Mock private AircraftRepository repository;
    @InjectMocks private UpdateAircraftStatusUseCase useCase;

    @Test void throwsWhenAircraftNotFound() {
        when(repository.findByRegistrationNumber(any(RegistrationNumber.class))).thenReturn(Optional.empty());
        UpdateAircraftStatusRequest req = new UpdateAircraftStatusRequest(AircraftStatus.IN_FLIGHT);
        assertThrows(IllegalArgumentException.class, () -> useCase.execute("CS-ZZZ", req));
    }

    @Test void updatesStatusSuccessfully() {
        Aircraft mockAircraft = mock(Aircraft.class);
        when(repository.findByRegistrationNumber(any(RegistrationNumber.class))).thenReturn(Optional.of(mockAircraft));
        when(repository.save(any(Aircraft.class))).thenReturn(mockAircraft);
        UpdateAircraftStatusRequest req = new UpdateAircraftStatusRequest(AircraftStatus.UNDER_MAINTENANCE);
        assertDoesNotThrow(() -> useCase.execute("CS-TPA", req));
        verify(repository).save(mockAircraft);
    }

    @Test void throwsWhenRequestIsNull() {
        assertThrows(Exception.class, () -> useCase.execute("CS-TPA", null));
    }
}